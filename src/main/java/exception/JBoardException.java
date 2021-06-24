package exception;

public class JBoardException extends java.lang.Exception {
    public JBoardException() {
        super();
    }

    public JBoardException(String message) {
        super(message);
    }

    public JBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getResponseCode(){
        return 500;
    }

    public String getResponseMessage(){
        return "Internal error.";
    }
}
