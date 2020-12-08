package Utils;

import Models.User;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class AuthManager {
    public static void setUser(HttpSession session, User user){
        session.setAttribute("userID", user.getID());
    }

    public static User getUser(HttpSession session) throws SQLException {
        Integer userID = (Integer)session.getAttribute("userID");
        return userID == null ? null : User.find(userID);
    }

    public static void dropUser(HttpSession session){
        session.removeAttribute("userID");
    }
}
