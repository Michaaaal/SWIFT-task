package michal.malek.remitlytask.exception;

/**
 * Exception class for not valid swift data.
 */
public class SwiftDataNotValidException extends RuntimeException {
    public SwiftDataNotValidException(String message) {
        super(message);
    }
}
