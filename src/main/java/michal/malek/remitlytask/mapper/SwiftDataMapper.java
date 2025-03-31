package michal.malek.remitlytask.mapper;

import michal.malek.remitlytask.model.swift_data.SwiftDataCsv;
import michal.malek.remitlytask.model.swift_data.SwiftDataDoc;
import michal.malek.remitlytask.model.swift_data.request.SwiftDataRequest;
import michal.malek.remitlytask.model.swift_data.response.HeadquarterAndBranchesResponse;
import michal.malek.remitlytask.model.swift_data.response.BranchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper for Swift data related classes.
 */
@Mapper(componentModel = "spring")
public interface SwiftDataMapper {

    /**
     * Maps a SwiftDataCsv object to a SwiftDataDoc object.
     * Sets the isHeadquarter field based on the swiftCode.
     */
    @Mapping(source = "address", target = "address")
    @Mapping(source = "name", target = "bankName")
    @Mapping(source = "countryISO2Code", target = "countryISO2")
    @Mapping(source = "countryName", target = "countryName")
    @Mapping(source = "swiftCode", target = "swiftCode")
    @Mapping(source = "swiftCode", target = "isHeadquarter", qualifiedByName = "checkIfHeadquarter")
    SwiftDataDoc swiftCsvToDocs(SwiftDataCsv swiftCsv);

    /**
     * Checks if the given swiftCode indicates a headquarters.
     * Returns true if the swiftCode ends with "XXX".
     * @param swiftCode the SWIFT code
     * @return true if it's a headquarters, false otherwise
     */
    @Named("checkIfHeadquarter")
    static boolean checkIfHeadquarter(String swiftCode) {
        return swiftCode != null && swiftCode.endsWith("XXX");
    }

    /**
     * Maps a list of SwiftDataCsv objects to a list of SwiftDataDoc objects.
     */
    List<SwiftDataDoc> swiftCsvListToDocsList(List<SwiftDataCsv> swiftDataDocs);

    /**
     * Maps only fields of Headquarters SwiftData, so need to set list separately.
     */
    HeadquarterAndBranchesResponse swiftDocToHeadquarter(SwiftDataDoc swiftDataDoc);

    /**
     * Maps a SwiftDataDoc to SwiftDataResponse.
     */
    BranchResponse swiftDocToResponse(SwiftDataDoc swiftDataDoc);

    /**
     * Maps List of SwiftDataDoc to List of SwiftDataResponse.
     */
    List<BranchResponse> swiftDocListToResponseList(List<SwiftDataDoc> swiftDataDocs);

    SwiftDataDoc swiftRequestToDoc(SwiftDataRequest swiftDataRequest);
}


