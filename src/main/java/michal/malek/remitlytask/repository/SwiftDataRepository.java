package michal.malek.remitlytask.repository;

import michal.malek.remitlytask.model.swift_data.SwiftDataDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for SwiftDataDoc class.
 */
@Repository
public interface SwiftDataRepository extends MongoRepository<SwiftDataDoc, String> {

    /**
     * Finds branch documents by matching bank name, country ISO2 and where isHeadquarter is false.
     */
    List<SwiftDataDoc> findByBankNameAndCountryISO2AndHeadquarter(String bankName, String countryISO2, boolean isHeadquarter);

    /**
     * Finds all documents by country ISO2 code.
     */
    List<SwiftDataDoc> findByCountryISO2(String countryISO2);

    /**
     * Finds docs which swift starts with provided prefix.
     */
    List<SwiftDataDoc> findBySwiftCodeStartingWithAndIsHeadquarterFalse(String prefix);
}
