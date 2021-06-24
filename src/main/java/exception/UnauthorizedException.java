package exception;

public class UnauthorizedException extends JBoardException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getResponseCode(){
        return 403;
    }

    public String getResponseMessage(){
        return "Unauthorized.";
    }
}
