package Controllers;

import Models.Teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class TeacherController extends Controller {
    public TeacherController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void createGET() throws IOException {
        String basePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
        this.serveFile(basePath + "/../views/teacher/creation.html");
    }

    public void createPOST() throws IOException {
        String name = this.request.getParameter("name");
        String surname = this.request.getParameter("surname");
        Teacher teacher = new Teacher();
        teacher.setName(name).setSurname(surname).save();
        this.response.sendRedirect("/es2_war/teacher/create");
    }
}
