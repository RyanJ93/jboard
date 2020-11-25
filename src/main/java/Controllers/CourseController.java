package Controllers;

import Models.Course;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class CourseController extends Controller {
    public CourseController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void createGET() throws IOException {
        String basePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
        this.serveFile(basePath + "/../views/course/creation.html");
    }

    public void createPOST() throws IOException {
        String title = this.request.getParameter("title");
        Course course = new Course();
        course.setTitle(title).save();
        this.response.sendRedirect("/es2_war/course/create");
    }
}
