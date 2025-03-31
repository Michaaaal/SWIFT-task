package michal.malek.remitlytask.model.swift_data.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

/**
 * Response class for country related swift data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "countryISO2", "countryName", "swiftCodes" })
public class CountrySwiftDataResponse {
    private String countryISO2;
    private String countryName;
    private List<BranchResponse> swiftCodes;
}
