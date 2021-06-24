package form;

import javax.servlet.http.HttpServletRequest;

public class CourseCreateForm extends Form {
    public CourseCreateForm(HttpServletRequest request){
        super(request);
    }

    public boolean validate(){
        String title = this.request.getParameter("title");
        this.dropMessages();
        if ( title == null || title.isEmpty() ){
            this.messages.put("title", "You must provide a valid title.");
        }
        return this.messages.isEmpty();
    }
}
