package form;

import javax.servlet.http.HttpServletRequest;

public class UserLoginForm extends Form {
    public UserLoginForm(HttpServletRequest request){
        super(request);
    }

    public boolean validate(){
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        this.dropMessages();
        if ( account == null || account.isEmpty() ){
            this.messages.put("account", "You must provide a valid account.");
        }
        if ( password == null || password.isEmpty() ){
            this.messages.put("password", "You must provide a valid password.");
        }
        return this.messages.isEmpty();
    }
}
