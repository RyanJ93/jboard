import controller.LessonController;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LessonMarkServlet", urlPatterns = "/api/lesson/mark")
public class LessonMarkServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LessonController lessonController = new LessonController(request, response);
        lessonController.mark();
    }
}
