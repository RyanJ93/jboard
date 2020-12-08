package Controllers;

import Models.Course;
import Models.Repetition;
import Models.Teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RepetitionController extends Controller {
    public RepetitionController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void list() throws IOException {
        try{
            this.checkAuth().adminRequired();
            ArrayList<Repetition> repetitions = Repetition.getAll();
            this.sendSuccessResponse(repetitions);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void delete() throws IOException {
        try{
            this.checkAuth().adminRequired();
            int repetitionID = Integer.parseInt(this.request.getParameter("id"));
            Repetition repetition = Repetition.find(repetitionID);
            if ( repetition != null ){
                repetition.delete();
            }
            this.sendSuccessResponse(null);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }

    public void create() throws IOException {
        try{
            this.checkAuth().adminRequired();
            int courseID = Integer.parseInt(this.request.getParameter("courseID"));
            int teacherID = Integer.parseInt(this.request.getParameter("teacherID"));
            Course course = Course.find(courseID);
            if ( course == null ){
                //
            }
            Teacher teacher = Teacher.find(teacherID);
            if ( teacher == null ){
                //
            }
            Repetition repetition = new Repetition();
            repetition.setCourse(course).setTeacher(teacher).save();
            this.sendSuccessResponse(repetition);
        }catch(SQLException ex){
            this.sendException(ex);
        }
    }
}
