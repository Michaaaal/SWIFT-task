package michal.malek.remitlytask.exception;

/**
 * Exception class for Csv parsing failure.
 */
public class CsvParsingException extends RuntimeException {
    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
