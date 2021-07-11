package form;

import exception.ModelException;
import model.Lesson;
import model.User;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

public class LessonCreateForm extends Form {
    private ArrayList<HashMap<String, Object>> availableLessons;
    private final User authenticatedUser;

    public LessonCreateForm(HttpServletRequest request, User authenticatedUser){
        super(request);
        this.authenticatedUser = authenticatedUser;
    }

    public LessonCreateForm loadAvailableLessons() throws ModelException {
        this.availableLessons = Lesson.getAvailable(this.authenticatedUser);
        return this;
    }

    public boolean validateAvailability() throws ModelException {
        int teacherID = Integer.parseInt(this.request.getParameter("teacherID"));
        int courseID = Integer.parseInt(this.request.getParameter("courseID"));
        int hour = Integer.parseInt(this.request.getParameter("hour"));
        int day = Integer.parseInt(this.request.getParameter("day"));
        return Lesson.isLessonAvailable(courseID, teacherID, hour, day);
    }

    public boolean validate(){
        return true;
    }
}
