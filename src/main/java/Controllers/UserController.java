package Controllers;

import Models.User;
import Utils.AuthManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class UserController extends Controller {
    public UserController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void login() throws IOException {
        try{
            String account = this.request.getParameter("account");
            String password = this.request.getParameter("password");
            User user = User.findByAccount(account);
            if ( user == null ){
                this.sendErrorResponse(null, 404);
            }else if ( !user.getPasswordCocktail().compare(password) ){
                this.sendErrorResponse(null, 403);
            }else{
                AuthManager.setUser(this.request.getSession(), user);
                HashMap<String, String> data = new HashMap<>();
                data.put("role", user.getRole());
                this.sendSuccessResponse(data);
            }
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void logout() throws IOException {
        AuthManager.dropUser(this.request.getSession());
        this.sendSuccessResponse(null);
    }
}
