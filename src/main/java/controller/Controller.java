package controller;

import model.User;
import model.UserToken;
import util.AuthManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import exception.ModelException;
import java.util.List;
import java.util.HashMap;
import exception.*;

public abstract class Controller {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected User authenticatedUser;

    protected void serveFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        PrintWriter output = this.response.getWriter();
        for ( String line : lines ){
            output.println(line);
        }
        output.close();
    }

    protected void sendResponse(HashMap<String, Object> properties) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        this.response.setHeader("Content-Type", "application/json");
        PrintWriter output = this.response.getWriter();
        output.println(gson.toJson(properties));
        output.close();
    }

    protected void sendException(Exception ex) throws IOException {
        String responseMessage = ex instanceof JBoardException ? ((JBoardException)ex).getResponseMessage() : ex.getMessage();
        int responseCode = ex instanceof JBoardException ? ((JBoardException)ex).getResponseCode() : 500;
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("code", responseCode);
        response.put("message", responseMessage);
        ex.printStackTrace();
        this.sendResponse(response);
    }

    protected void sendFormErrorMessages(HashMap<String, String> messages) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("code", 400);
        response.put("messages", messages);
        this.sendResponse(response);
    }

    protected void sendErrorResponse(String message, int code) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("code", code);
        if ( message != null && message.length() > 0 ){
            response.put("message", message);
        }
        this.sendResponse(response);
    }

    protected void sendSuccessResponse(Object data) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("code", 200);
        if ( data != null ){
            response.put("data", data);
        }
        this.sendResponse(response);
    }

    protected Controller checkAuth() throws ModelException, UnauthorizedException {
        String token = this.request.getParameter("token");
        if ( token != null && !token.isEmpty() ){
            UserToken userToken = UserToken.find(token);
            this.authenticatedUser = userToken != null ? userToken.getUser() : null;
        }else{
            this.authenticatedUser = AuthManager.getUser(this.request.getSession());
        }
        if ( this.authenticatedUser == null ){
            throw new UnauthorizedException("Unauthorized");
        }
        return this;
    }

    protected Controller adminRequired() throws UnauthorizedException {
        if ( this.authenticatedUser == null || !this.authenticatedUser.getRole().equals("admin") ){
            throw new UnauthorizedException("Unauthorized");
        }
        return this;
    }

    Controller(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }
}
