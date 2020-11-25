package Controllers;

import Models.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class UserController extends Controller {
    public UserController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void login() throws IOException {
        String account = this.request.getParameter("account");
        String password = this.request.getParameter("password");
        User user = User.findByAccount(account);
        if ( user == null ){
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("code", 404);
            this.sendResponse(response);
            return;
        }
        /*
        if ( !user.getPasswordCocktail().compare(password) ){
            //
        }*/
    }
}
