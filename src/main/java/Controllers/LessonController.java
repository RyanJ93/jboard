package Controllers;

import Models.Course;
import Models.Lesson;
import Models.Teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LessonController extends Controller {
    public LessonController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void create() throws IOException {
        try{
            this.checkAuth();
            int hour = Integer.parseInt(this.request.getParameter("hour"));
            int day = Integer.parseInt(this.request.getParameter("day"));
            int courseID = Integer.parseInt(this.request.getParameter("courseID"));
            int teacherID = Integer.parseInt(this.request.getParameter("teacherID"));
            Course course = Course.find(courseID);
            if ( course == null ){
                this.sendErrorResponse("No such course found.", 401);
                return;
            }
            Teacher teacher = Teacher.find(teacherID);
            if ( teacher == null ){
                this.sendErrorResponse("No such teacher found.", 402);
                return;
            }
            Lesson lesson = new Lesson();
            lesson.setHour(hour).setDay(day).setUser(this.authenticatedUser).setCourse(course).setTeacher(teacher).save();
            this.sendSuccessResponse(lesson);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void list() throws IOException {
        try{
            this.checkAuth();
            ArrayList<Lesson> lessons = Lesson.getAllByUser(this.authenticatedUser, true);
            this.sendSuccessResponse(lessons);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void listAll() throws IOException {
        try{
            this.checkAuth().adminRequired();
            ArrayList<Lesson> lessons = Lesson.getAllByUser(null, true);
            for ( Lesson lesson : lessons ){
                lesson.getUser().setPassword(null);
            }
            this.sendSuccessResponse(lessons);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void mark() throws IOException {
        try{
            this.checkAuth();
            int lessonID = Integer.parseInt(this.request.getParameter("id"));
            boolean completed = this.request.getParameter("completed").equals("true");
            Lesson lesson = Lesson.find(lessonID);
            if ( lesson == null ){
                this.sendErrorResponse("No such lesson found.", 404);
                return;
            }
            if ( lesson.getUser().getID() != this.authenticatedUser.getID() && !this.authenticatedUser.getRole().equals("admin") ){
                this.sendErrorResponse("Permission denied.", 405);
                return;
            }
            lesson.setCompleted(completed).save();
            this.sendSuccessResponse(null);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void delete() throws IOException {
        try{
            this.checkAuth();
            int lessonID = Integer.parseInt(this.request.getParameter("id"));
            Lesson lesson = Lesson.find(lessonID);
            if ( lesson != null ){
                if ( lesson.getUser().getID() != this.authenticatedUser.getID() && !this.authenticatedUser.getRole().equals("admin") ){
                    this.sendErrorResponse("Permission denied.", 405);
                    return;
                }
                lesson.delete(false);
            }
            this.sendSuccessResponse(null);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }
}
