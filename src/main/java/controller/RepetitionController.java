package controller;

import form.RepetitionCreateForm;
import model.Course;
import model.Repetition;
import model.Teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class RepetitionController extends Controller {
    public RepetitionController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void list() throws IOException {
        try{
            this.checkAuth().adminRequired();
            ArrayList<Repetition> repetitions = Repetition.getAll(false);
            this.sendSuccessResponse(repetitions);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void delete() throws IOException {
        try{
            this.checkAuth().adminRequired();
            int repetitionID = Integer.parseInt(this.request.getParameter("id"));
            Repetition repetition = Repetition.find(repetitionID);
            if ( repetition != null ){
                repetition.delete(true);
            }
            this.sendSuccessResponse(null);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void create() throws IOException {
        try{
            this.checkAuth().adminRequired();
            RepetitionCreateForm repetitionCreateForm = new RepetitionCreateForm(this.request);
            if ( !repetitionCreateForm.validate() ){
                this.sendFormErrorMessages(repetitionCreateForm.getMessages());
                return;
            }
            int courseID = Integer.parseInt(this.request.getParameter("courseID"));
            int teacherID = Integer.parseInt(this.request.getParameter("teacherID"));
            Course course = Course.find(courseID);
            if ( course == null ){
                repetitionCreateForm.dropMessages().setFieldMessage("courseID", "You must select an existing course.");
                this.sendFormErrorMessages(repetitionCreateForm.getMessages());
                return;
            }
            Teacher teacher = Teacher.find(teacherID);
            if ( teacher == null ){
                repetitionCreateForm.dropMessages().setFieldMessage("teacherID", "You must select an existing teacher.");
                this.sendFormErrorMessages(repetitionCreateForm.getMessages());
                return;
            }
            Repetition repetition = new Repetition();
            repetition.setCourse(course).setTeacher(teacher).save();
            this.sendSuccessResponse(repetition);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }
}
