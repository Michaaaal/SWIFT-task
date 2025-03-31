package michal.malek.remitlytask.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import michal.malek.remitlytask.model.constant.ValidationConstants;
import michal.malek.remitlytask.model.constant.messages.ValidationMessage;
import michal.malek.remitlytask.model.standard.StandardizedSuccessResponse;
import michal.malek.remitlytask.model.swift_data.request.SwiftDataRequest;
import michal.malek.remitlytask.model.swift_data.response.CountrySwiftDataResponse;
import michal.malek.remitlytask.model.swift_data.response.HeadquarterAndBranchesResponse;
import michal.malek.remitlytask.service.SwiftDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Main controller of application.
 * Responsible for swift data retrieval and addition.
 */
@RequestMapping("/v1/swift-codes")
@RestController
@Validated
@RequiredArgsConstructor
public class SwiftController {
    private final SwiftDataService swiftDataService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<HeadquarterAndBranchesResponse> getSwiftCode(
            @NotNull(message = ValidationMessage.CANT_BE_BLANK)
            @Pattern(regexp = ValidationConstants.SWIFT_REGEX, message = ValidationMessage.WRONG_SWIFT_FORMAT)
            @PathVariable("swiftCode") String swiftCode) {
        HeadquarterAndBranchesResponse swiftDataWithBranches = swiftDataService.getSwiftDataWithBranches(swiftCode);
        return ResponseEntity.ok().body(swiftDataWithBranches);
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftDataResponse> getSwiftCodesByCountry(
            @NotNull(message = ValidationMessage.CANT_BE_BLANK)
            @Pattern(regexp = ValidationConstants.ISO2_REGEX, message = ValidationMessage.WRONG_ISO2_FORMAT)
            @PathVariable("countryISO2code") String countryISO2code) {
        CountrySwiftDataResponse countrySwiftData = swiftDataService.getCountrySwiftData(countryISO2code);
        return ResponseEntity.ok().body(countrySwiftData);
    }

    @PostMapping
    public ResponseEntity<StandardizedSuccessResponse> addSwiftCode(@Validated @RequestBody SwiftDataRequest request) {
        StandardizedSuccessResponse response = swiftDataService.addSwiftData(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<StandardizedSuccessResponse> deleteSwiftCode(
            @NotNull(message = ValidationMessage.CANT_BE_BLANK)
            @Pattern(regexp = ValidationConstants.SWIFT_REGEX, message = ValidationMessage.WRONG_SWIFT_FORMAT)
            @PathVariable("swiftCode") String swiftCode) {
        StandardizedSuccessResponse response = swiftDataService.deleteSwiftData(swiftCode);
        return ResponseEntity.ok().body(response);
    }
}
