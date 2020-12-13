package Forms;

import javax.servlet.http.HttpServletRequest;

public class TeacherCreateForm extends Form {
    public TeacherCreateForm(HttpServletRequest request){
        super(request);
    }

    public boolean validate(){
        String name = this.request.getParameter("name");
        String surname = this.request.getParameter("surname");
        this.dropMessages();
        if ( name == null || name.isEmpty() ){
            this.messages.put("name", "You must provide a valid name.");
        }
        if ( name == null || name.isEmpty() ){
            this.messages.put("name", "You must provide a valid name.");
        }
        return this.messages.isEmpty();
    }
}
