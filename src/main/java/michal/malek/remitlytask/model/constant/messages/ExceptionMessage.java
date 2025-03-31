package michal.malek.remitlytask.model.constant.messages;

/**
 * Class for centralization of exception messages.
 */
public class ExceptionMessage {
    public static final String CSV_READ_FAILURE = "Csv Read Failed at %s with class %s";
    public static final String CSV_PROCESSING_FAILURE = "Csv Processing Failed at %s with class %s";
    public static final String BULK_OPERATION_FAILURE = "Error executing bulk operations at %s";
    public static final String SWIFT_DATA_NOT_FOUND = "SWIFT: %s, data not found";
    public static final String SWIFT_DATA_NOT_FOUND_ISO2 = "for ISO2: %s, data not found";
    public static final String SWIFT_CODE_ALREADY_EXISTS = "Record with Swift-code: %s already exist";
    public static final String WRONG_XXX_USE = "Swift code ending with 'XXX' must be used for headquarters";
}
