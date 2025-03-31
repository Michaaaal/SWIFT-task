package michal.malek.remitlytask.model.constant.messages;

/**
 * Class for centralization of validation messages.
 */
public class ValidationMessage {
    public static final String WRONG_SWIFT_FORMAT = "Must be containing capital letters or numbers, and have 8-11 length";
    public static final String WRONG_ISO2_FORMAT = "Country ISO2 code must consist of two uppercase letters";
    public static final String CANT_BE_BLANK = "Parameter can't be blank";
}
