package Controllers;

import Models.User;
import Utils.AuthManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

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
        Gson gson = builder.create();
        this.response.setHeader("Content-Type", "application/json");
        PrintWriter output = this.response.getWriter();
        output.println(gson.toJson(properties));
        output.close();
    }

    protected void sendException(Exception ex) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("code", 500);
        response.put("message", ex.getMessage());
        ex.printStackTrace();
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

    protected Controller checkAuth() throws IOException, SQLException {
        this.authenticatedUser = AuthManager.getUser(this.request.getSession());
        if ( this.authenticatedUser == null ){
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("code", 403);
            this.sendResponse(response);
        }
        return this;
    }

    protected Controller adminRequired() throws IOException {
        if ( this.authenticatedUser == null || !this.authenticatedUser.getRole().equals("admin") ){
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("code", 403);
            this.sendResponse(response);
        }
        return this;
    }

    Controller(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }
}
