package michal.malek.remitlytask.exception;

/**
 * Exception class for not finding SwiftData.
 */
public class SwiftDataNotFoundException extends RuntimeException {
    public SwiftDataNotFoundException(String message) {
        super(message);
    }
}
