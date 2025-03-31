package michal.malek.remitlytask.exception;

/**
 * Exception class for Bulk Ops failure.
 */
public class BulkOperationException extends RuntimeException {
    public BulkOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
