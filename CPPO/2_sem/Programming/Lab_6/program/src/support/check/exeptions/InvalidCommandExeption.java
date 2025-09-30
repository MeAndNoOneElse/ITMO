package support.check.exeptions;

public class InvalidCommandExeption extends RuntimeException {
    public InvalidCommandExeption(String message) {
        super(message);
    }
}
