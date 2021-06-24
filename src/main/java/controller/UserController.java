package controller;

import form.UserLoginForm;
import model.User;
import model.UserToken;
import util.AuthManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class UserController extends Controller {
    public UserController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void login() throws IOException {
        try{
            UserLoginForm userLoginForm = new UserLoginForm(this.request);
            if ( !userLoginForm.validate() ){
                this.sendFormErrorMessages(userLoginForm.getMessages());
                return;
            }
            String account = this.request.getParameter("account");
            String password = this.request.getParameter("password");
            User user = User.findByAccount(account);
            if ( user == null ){
                this.sendErrorResponse(null, 404);
            }else if ( !user.getPasswordCocktail().compare(password) ){
                this.sendErrorResponse(null, 403);
            }else{
                String useAuthToken = this.request.getParameter("useAuthToken");
                HashMap<String, Object> data = new HashMap<>();
                data.put("user", user);
                if ( useAuthToken != null && useAuthToken.equals("1") ){
                    UserToken userToken = AuthManager.makeUserToken(user);
                    data.put("token", userToken.getToken());
                }else{
                    AuthManager.setUser(this.request.getSession(), user);
                }
                this.sendSuccessResponse(data);
            }
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void logout() throws IOException {
        try{
            String token = this.request.getParameter("token");
            if ( token != null && !token.isEmpty() ){
                UserToken userToken = UserToken.find(token);
                if ( userToken != null ){
                    userToken.delete();
                }
            }else{
                AuthManager.dropUser(this.request.getSession());
            }
            this.sendSuccessResponse(null);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void get() throws IOException {
        try{
            this.checkAuth();
            HashMap<String, Object> data = new HashMap<>();
            data.put("user", this.authenticatedUser);
            this.sendSuccessResponse(data);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }
}
