import Controllers.UserController;
import Support.Database;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserLogoutServlet", urlPatterns = "/api/user/logout")
public class UserLogoutServlet extends HttpServlet {
    public void init(ServletConfig c){
        Database.setup();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserController userController = new UserController(request, response);
        userController.logout();
    }
}
