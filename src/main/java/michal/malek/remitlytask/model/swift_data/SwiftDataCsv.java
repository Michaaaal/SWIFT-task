package michal.malek.remitlytask.model.swift_data;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

/**
 * Class for csv representation of swift data.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SwiftDataCsv {
    @CsvBindByName(column = "COUNTRY ISO2 CODE")
    private String countryISO2Code;

    @CsvBindByName(column = "SWIFT CODE")
    private String swiftCode;

    @CsvBindByName(column = "CODE TYPE")
    private String codeType;

    @CsvBindByName(column = "NAME")
    private String name;

    @CsvBindByName(column = "ADDRESS")
    private String address;

    @CsvBindByName(column = "TOWN NAME")
    private String townName;

    @CsvBindByName(column = "COUNTRY NAME")
    private String countryName;

    @CsvBindByName(column = "TIME ZONE")
    private String timeZone;
}
