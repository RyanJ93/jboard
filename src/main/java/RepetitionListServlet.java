import Controllers.RepetitionController;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RepetitionListServlet", urlPatterns = "/api/repetition/list")
public class RepetitionListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RepetitionController repetitionController = new RepetitionController(request, response);
        repetitionController.list();
    }
}
