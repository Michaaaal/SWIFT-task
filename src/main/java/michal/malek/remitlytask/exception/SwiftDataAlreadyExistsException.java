package michal.malek.remitlytask.exception;

/**
 * Exception for duplicate swift value input.
 */
public class SwiftDataAlreadyExistsException extends RuntimeException{
    public SwiftDataAlreadyExistsException(String message) {
        super(message);
    }
}
