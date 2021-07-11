package controller;

import form.LessonCreateForm;
import model.Course;
import model.Lesson;
import model.Teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class LessonController extends Controller {
    public LessonController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void create() throws IOException {
        try{
            this.checkAuth();
            int teacherID = Integer.parseInt(this.request.getParameter("teacherID"));
            int courseID = Integer.parseInt(this.request.getParameter("courseID"));
            int hour = Integer.parseInt(this.request.getParameter("hour"));
            int day = Integer.parseInt(this.request.getParameter("day"));
            LessonCreateForm lessonCreateForm = new LessonCreateForm(this.request, this.authenticatedUser);
            if ( !lessonCreateForm.validateAvailability() ){
                this.sendErrorResponse("The selected time slot is not available.", 400);
                return;
            }
            Course course = Course.find(courseID);
            if ( course == null ){
                this.sendErrorResponse("No such course found.", 400);
                return;
            }
            Teacher teacher = Teacher.find(teacherID);
            if ( teacher == null ){
                this.sendErrorResponse("No such teacher found.", 400);
                return;
            }
            Lesson lesson = new Lesson();
            lesson.setHour(hour).setDay(day).setUser(this.authenticatedUser).setCourse(course).setTeacher(teacher).save();
            this.sendSuccessResponse(lesson);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void list() throws IOException {
        try{
            this.checkAuth();
            ArrayList<Lesson> lessons = Lesson.getAllByUser(this.authenticatedUser, true);
            this.sendSuccessResponse(lessons);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void listAll() throws IOException {
        try{
            this.checkAuth().adminRequired();
            ArrayList<Lesson> lessons = Lesson.getAllByUser(null, true);
            this.sendSuccessResponse(lessons);
        }catch(Exception ex){
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
        }catch(Exception ex){
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
        }catch(Exception ex){
            this.sendException(ex);
        }
    }
}
