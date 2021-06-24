package exception;

public class DuplicateEntryModelException extends JBoardException {
    public DuplicateEntryModelException() {
        super();
    }

    public DuplicateEntryModelException(String message) {
        super(message);
    }

    public DuplicateEntryModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getResponseCode(){
        return 400;
    }

    public String getResponseMessage(){
        String message = this.getMessage();
        return message != null && !message.isEmpty() ? message : "Duplicate entry.";
    }
}
