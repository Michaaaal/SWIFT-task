package michal.malek.remitlytask.model.swift_data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class representing mongoDb document of swift data.
 */
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class SwiftDataDoc {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    @Id
    private String swiftCode;
}
