package Controllers;

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
import java.util.List;
import java.util.HashMap;

public abstract class Controller {
    protected HttpServletRequest request;
    protected HttpServletResponse response;

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
        PrintWriter output = this.response.getWriter();
        output.println(gson.toJson(properties));
        output.close();
    }

    Controller(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }
}
