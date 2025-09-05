package support.check.exeptions;

public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
