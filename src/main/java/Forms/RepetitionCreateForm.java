package Forms;

import javax.servlet.http.HttpServletRequest;

public class RepetitionCreateForm extends Form {
    public RepetitionCreateForm(HttpServletRequest request){
        super(request);
    }

    public boolean validate(){
        int courseID = Integer.parseInt(this.request.getParameter("courseID"));
        int teacherID = Integer.parseInt(this.request.getParameter("teacherID"));
        if ( courseID <= 0 ){
            this.messages.put("courseID", "You must select an existing course.");
        }
        if ( teacherID <= 0 ){
            this.messages.put("teacherID", "You must select an existing teacher.");
        }
        return this.messages.isEmpty();
    }
}
