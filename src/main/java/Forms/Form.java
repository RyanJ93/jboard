package Forms;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public abstract class Form {
    protected HashMap<String, String> messages = new HashMap<>();
    protected HttpServletRequest request;

    public Form(HttpServletRequest request){
        this.request = request;
    }

    public Form dropMessages(){
        this.messages.clear();
        return this;
    }

    public Form setFieldMessage(String field, String message){
        this.messages.put(field, message);
        return this;
    }

    public HashMap<String, String> getMessages(){
        return this.messages;
    }

    public boolean hasError(){
        return !this.messages.isEmpty();
    }

    public abstract boolean validate();
}
