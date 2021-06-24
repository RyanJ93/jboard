import controller.LessonController;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LessonListAllServlet", urlPatterns = "/api/lesson/list-all")
public class LessonListAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LessonController lessonController = new LessonController(request, response);
        lessonController.listAll();
    }
}
