package Controllers;

import Forms.TeacherCreateForm;
import Models.Teacher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TeacherController extends Controller {
    public TeacherController(HttpServletRequest request, HttpServletResponse response){
        super(request, response);
    }

    public void list() throws IOException {
        try{
            this.checkAuth().adminRequired();
            ArrayList<Teacher> teachers = Teacher.getAll();
            this.sendSuccessResponse(teachers);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void delete() throws IOException {
        try{
            this.checkAuth().adminRequired();
            int teacherID = Integer.parseInt(this.request.getParameter("id"));
            Teacher teacher = Teacher.find(teacherID);
            if ( teacher != null ){
                teacher.delete();
            }
            this.sendSuccessResponse(null);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }

    public void create() throws IOException {
        try{
            this.checkAuth().adminRequired();
            TeacherCreateForm teacherCreateForm = new TeacherCreateForm(this.request);
            if ( !teacherCreateForm.validate() ){
                this.sendFormErrorMessages(teacherCreateForm.getMessages());
                return;
            }
            String name = this.request.getParameter("name");
            String surname = this.request.getParameter("surname");
            Teacher teacher = new Teacher();
            teacher.setName(name).setSurname(surname).save();
            this.sendSuccessResponse(teacher);
        }catch(Exception ex){
            this.sendException(ex);
        }
    }
}
