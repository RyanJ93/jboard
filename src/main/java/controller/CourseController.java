package controller;

import form.CourseCreateForm;
import model.Course;
import model.Lesson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseController extends Controller {
    public CourseController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void available() throws IOException {
        try{
            this.checkAuth();
            ArrayList<HashMap<String, Object>> availableLessons = Lesson.getAvailable(this.authenticatedUser);
            this.sendSuccessResponse(availableLessons);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void list() throws IOException {
        try{
            this.checkAuth().adminRequired();
            ArrayList<Course> courses = Course.getAll();
            this.sendSuccessResponse(courses);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void delete() throws IOException {
        try{
            this.checkAuth().adminRequired();
            int courseID = Integer.parseInt(this.request.getParameter("id"));
            Course course = Course.find(courseID);
            if ( course != null ){
                course.delete();
            }
            this.sendSuccessResponse(null);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void create() throws IOException {
        try{
            this.checkAuth().adminRequired();
            CourseCreateForm courseCreateForm = new CourseCreateForm(this.request);
            if ( !courseCreateForm.validate() ){
                this.sendFormErrorMessages(courseCreateForm.getMessages());
                return;
            }
            String title = this.request.getParameter("title");
            Course course = new Course();
            course.setTitle(title).save();
            this.sendSuccessResponse(course);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }
}
