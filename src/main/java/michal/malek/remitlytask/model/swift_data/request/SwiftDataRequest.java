package michal.malek.remitlytask.model.swift_data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import michal.malek.remitlytask.model.constant.ValidationConstants;
import michal.malek.remitlytask.model.constant.messages.ValidationMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SwiftDataRequest {
    @NotBlank(message = ValidationMessage.CANT_BE_BLANK)
    private String address;

    @NotBlank(message = ValidationMessage.CANT_BE_BLANK)
    private String bankName;

    @NotBlank(message = ValidationMessage.CANT_BE_BLANK)
    @Pattern(regexp = ValidationConstants.ISO2_REGEX, message = ValidationMessage.WRONG_ISO2_FORMAT)
    private String countryISO2;

    @NotBlank(message = ValidationMessage.CANT_BE_BLANK)
    private String countryName;

    @NotNull(message = ValidationMessage.CANT_BE_BLANK)
    private Boolean isHeadquarter;

    @NotBlank(message = ValidationMessage.CANT_BE_BLANK)
    @Pattern(regexp = ValidationConstants.SWIFT_REGEX, message = ValidationMessage.WRONG_SWIFT_FORMAT)
    private String swiftCode;
}
