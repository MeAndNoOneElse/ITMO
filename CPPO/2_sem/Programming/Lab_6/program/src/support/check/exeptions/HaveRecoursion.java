package support.check.exeptions;

public class HaveRecoursion extends RuntimeException {
    public HaveRecoursion(String message) {
        super(message);
    }
}
