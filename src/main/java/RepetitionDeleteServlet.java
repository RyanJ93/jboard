import controller.RepetitionController;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RepetitionDeleteServlet", urlPatterns = "/api/repetition/delete")
public class RepetitionDeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RepetitionController repetitionController = new RepetitionController(request, response);
        repetitionController.delete();
    }
}
