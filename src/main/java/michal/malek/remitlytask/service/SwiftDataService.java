package michal.malek.remitlytask.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import michal.malek.remitlytask.exception.BulkOperationException;
import michal.malek.remitlytask.exception.SwiftDataAlreadyExistsException;
import michal.malek.remitlytask.exception.SwiftDataNotFoundException;
import michal.malek.remitlytask.exception.SwiftDataNotValidException;
import michal.malek.remitlytask.mapper.SwiftDataMapper;
import michal.malek.remitlytask.mapper.SwiftDataPersistenceMapper;
import michal.malek.remitlytask.model.constant.messages.ExceptionMessage;
import michal.malek.remitlytask.model.constant.messages.SuccessMessage;
import michal.malek.remitlytask.model.constant.messages.ValidationMessage;
import michal.malek.remitlytask.model.standard.StandardizedSuccessResponse;
import michal.malek.remitlytask.model.swift_data.SwiftDataCsv;
import michal.malek.remitlytask.model.swift_data.SwiftDataDoc;
import michal.malek.remitlytask.model.swift_data.request.SwiftDataRequest;
import michal.malek.remitlytask.model.swift_data.response.BranchResponse;
import michal.malek.remitlytask.model.swift_data.response.CountrySwiftDataResponse;
import michal.malek.remitlytask.model.swift_data.response.HeadquarterAndBranchesResponse;
import michal.malek.remitlytask.repository.SwiftDataRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service responsible for operations on Swift data.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SwiftDataService {
    private final Resource resource;
    private final CsvService csvService;
    private final SwiftDataRepository swiftDataRepository;
    private final SwiftDataPersistenceMapper swiftDataPersistenceMapper;
    private final SwiftDataMapper swiftDataMapper;
    private final MongoTemplate mongoTemplate;

    /**
     * Responsible for inserting data from CSV.
     * I used BulkOperations to reduce the number of round-trips to MongoDB by
     * aggregating multiple operations into a single request, which improves performance.
     */
    @PostConstruct
    private void initSwiftFromCsv(){
        List<SwiftDataCsv> csv = getCsv();
        List<SwiftDataDoc> swiftDataDocs = swiftDataMapper.swiftCsvListToDocsList(csv);

        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, SwiftDataDoc.class);

        for (SwiftDataDoc doc : swiftDataDocs) {
            Query query = swiftDataPersistenceMapper.createQueryForDoc(doc);
            Update update = swiftDataPersistenceMapper.mapToUpdate(doc);
            bulkOps.upsert(query, update);
        }

        try {
            bulkOps.execute();
        } catch (Exception e) {
            String message = String.format(ExceptionMessage.BULK_OPERATION_FAILURE, new Date());
            log.error(message, e);
            throw new BulkOperationException(message, e);
        }
    }

    /**
     * Gets headquarter Swift data with branches if applicable.
     * @param swiftCode the Swift code
     * @return Headquarters with details and branches
     */
    public HeadquarterAndBranchesResponse getSwiftDataWithBranches(String swiftCode){
        SwiftDataDoc swiftData = getBySwift(swiftCode);
        HeadquarterAndBranchesResponse headquarterResponse = swiftDataMapper.swiftDocToHeadquarter(swiftData);

        if(swiftData.isHeadquarter()){
            List<SwiftDataDoc> branches = getBranchesByHeadquartersSwift(swiftCode);
            List<BranchResponse> branchResponses = swiftDataMapper.swiftDocListToResponseList(branches);
            headquarterResponse.setBranches(branchResponses);
            return headquarterResponse;
        }
        return headquarterResponse;
    }

    /**
     * Retrieves country Swift data by ISO2 code.
     * If none found, throws SwiftDataNotFoundException.
     * @param countryISO2Code the 2-letter country code
     * @return CountrySwiftDataResponse with country and Swift data
     * @throws SwiftDataNotFoundException if no data is found
     */
    public CountrySwiftDataResponse getCountrySwiftData(String countryISO2Code){
        List<SwiftDataDoc> byCountryISO2 = swiftDataRepository.findByCountryISO2(countryISO2Code);
        if(byCountryISO2.isEmpty()){
            String message = String.format(ExceptionMessage.SWIFT_DATA_NOT_FOUND_ISO2, countryISO2Code);
            throw new SwiftDataNotFoundException(message);
        }
        return CountrySwiftDataResponse.builder()
                .countryISO2(countryISO2Code)
                .countryName(byCountryISO2.get(0).getCountryName())
                .swiftCodes(swiftDataMapper.swiftDocListToResponseList(byCountryISO2))
                .build();
    }

    /**
     * Adds new record of swift data to DB.
     * @param request SwiftDataRequest
     * @return success message
     */
    public StandardizedSuccessResponse addSwiftData(SwiftDataRequest request){
        validateSwiftDataRequest(request);
        SwiftDataDoc swiftDataDoc = swiftDataMapper.swiftRequestToDoc(request);
        swiftDataRepository.save(swiftDataDoc);
        String message = String.format(SuccessMessage.SWIFT_DATA_ADDITION_SUCCESS, swiftDataDoc.getSwiftCode());
        return new StandardizedSuccessResponse(message);
    }

    /**
     * Deletes swift data by code from DB.
     * @throws SwiftDataNotFoundException when data is not found
     * @param swiftCode swift code
     * @return success message
     */
    public StandardizedSuccessResponse deleteSwiftData(String swiftCode){
        if(!swiftDataRepository.existsById(swiftCode)){
            String message = String.format(ExceptionMessage.SWIFT_DATA_NOT_FOUND, swiftCode);
            throw new SwiftDataNotFoundException(message);
        }
        swiftDataRepository.deleteById(swiftCode);
        String message = String.format(SuccessMessage.SWIFT_DATA_DELETE_SUCCESS, swiftCode);
        return new StandardizedSuccessResponse(message);
    }

    /**
     * Validates if XXX is used properly and if there is no record in DB with the same Swift code.
     * @param request SwiftDataRequest
     */
    private void validateSwiftDataRequest(SwiftDataRequest request){
        String swiftCode = request.getSwiftCode();
        boolean isHeadquarters = request.getIsHeadquarter();

        if(swiftCode.endsWith("XXX") != isHeadquarters){
            throw new SwiftDataNotValidException(ExceptionMessage.WRONG_XXX_USE);
        }

        if (swiftDataRepository.existsById(swiftCode)) {
            throw new SwiftDataAlreadyExistsException(
                    String.format(ExceptionMessage.SWIFT_CODE_ALREADY_EXISTS, swiftCode));
        }
    }

    /**
     * Retrieves SwiftDataDoc from db.
     * @param swiftCode Swift code
     * @return SwiftDataDoc
     */
    private SwiftDataDoc getBySwift(String swiftCode){
        return swiftDataRepository.findById(swiftCode)
                .orElseThrow(() -> new SwiftDataNotFoundException(String.format(ExceptionMessage.SWIFT_DATA_NOT_FOUND, swiftCode)));
    }

    /**
     * Gets branches based on the headquarters Swift prefix.
     * @param swiftCode the headquarters Swift code
     * @return list of branches (SwiftDataDoc)
     */
    private List<SwiftDataDoc> getBranchesByHeadquartersSwift(String swiftCode){
        int SWIFT_PREFIX_LENGTH = 8;
        String swiftPrefix = swiftCode.substring(0 ,SWIFT_PREFIX_LENGTH);
        return swiftDataRepository.findBySwiftCodeStartingWithAndIsHeadquarterFalse(swiftPrefix);
    }

    /**
     * Provides List of Swift data.
     * @return List of SwiftDataCsv
     */
    private List<SwiftDataCsv> getCsv(){
        return csvService.readCsv(SwiftDataCsv.class, resource);
    }
}
