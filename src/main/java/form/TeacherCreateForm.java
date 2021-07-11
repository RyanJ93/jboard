package form;

import javax.servlet.http.HttpServletRequest;

public class TeacherCreateForm extends Form {
    public TeacherCreateForm(HttpServletRequest request){
        super(request);
    }

    public boolean validate(){
        String name = this.request.getParameter("name");
        String surname = this.request.getParameter("surname");
        this.dropMessages();
        if ( surname == null || surname.isEmpty() ){
            this.messages.put("surname", "You must provide a valid surname.");
        }
        if ( name == null || name.isEmpty() ){
            this.messages.put("name", "You must provide a valid name.");
        }
        return this.messages.isEmpty();
    }
}
