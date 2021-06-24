package util;

import model.User;
import model.UserToken;
import exception.ModelException;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

public class AuthManager {
    public static void setUser(HttpSession session, User user){
        session.setAttribute("userID", user.getID());
    }

    public static UserToken makeUserToken(User user) throws NoSuchAlgorithmException, ModelException {
        UserToken userToken = new UserToken();
        return userToken.setUser(user).generateToken().save();
    }

    public static User getUser(HttpSession session) throws ModelException {
        Integer userID = (Integer)session.getAttribute("userID");
        return userID == null ? null : User.find(userID);
    }

    public static void dropUser(HttpSession session){
        session.removeAttribute("userID");
    }
}
