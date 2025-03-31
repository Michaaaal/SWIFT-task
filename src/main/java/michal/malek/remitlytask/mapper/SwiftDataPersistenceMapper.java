package michal.malek.remitlytask.mapper;

import michal.malek.remitlytask.model.swift_data.SwiftDataDoc;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting SwiftDataDoc to MongoDB Query and Update objects.
 */
@Component
public class SwiftDataPersistenceMapper {

    /**
     * Creates a query to find a document by its swiftCode.
     * @param doc the SwiftDataDoc object
     * @return Query based on the _id field
     */
    public Query createQueryForDoc(SwiftDataDoc doc) {
        return new Query(Criteria.where("_id").is(doc.getSwiftCode()));
    }

    /**
     * Maps a SwiftDataDoc to an Update object.
     * @param doc the SwiftDataDoc object
     * @return Update with all necessary fields set
     */
    public Update mapToUpdate(SwiftDataDoc doc) {
        return new Update()
                .set("bankName", doc.getBankName())
                .set("address", doc.getAddress())
                .set("countryISO2", doc.getCountryISO2())
                .set("countryName", doc.getCountryName())
                .set("isHeadquarter", doc.isHeadquarter());
    }
}
