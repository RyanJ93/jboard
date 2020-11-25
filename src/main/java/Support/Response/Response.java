package Support.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Response {
    public abstract int getCode();

    public String toJSON(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}
