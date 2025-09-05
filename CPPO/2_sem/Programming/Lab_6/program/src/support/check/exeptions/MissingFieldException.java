package support.check.exeptions;

public class MissingFieldException extends IllegalArgumentException {
    public MissingFieldException(String message) {
        super(message);
    }
}
