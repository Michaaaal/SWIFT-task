package michal.malek.remitlytask.service.csv;

import michal.malek.remitlytask.exception.CsvParsingException;
import michal.malek.remitlytask.model.swift_data.SwiftDataCsv;
import michal.malek.remitlytask.service.CsvService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Tests CSV parsing for SwiftDataCsv using CsvService.
 */
public class SwiftDataCsvTest {

    private static final String COL_COUNTRY_ISO2_CODE = "COUNTRY ISO2 CODE";
    private static final String COL_SWIFT_CODE = "SWIFT CODE";
    private static final String COL_CODE_TYPE = "CODE TYPE";
    private static final String COL_NAME = "NAME";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_TOWN_NAME = "TOWN NAME";
    private static final String COL_COUNTRY_NAME = "COUNTRY NAME";
    private static final String COL_TIME_ZONE = "TIME ZONE";

    private static final String ROW1_COUNTRY_ISO2_CODE = "US";
    private static final String ROW1_SWIFT_CODE = "ABCDUS33";
    private static final String ROW1_CODE_TYPE = "TYPE1";
    private static final String ROW1_NAME = "Example Bank";
    private static final String ROW1_ADDRESS = "1234 Street";
    private static final String ROW1_TOWN_NAME = "City";
    private static final String ROW1_COUNTRY_NAME = "United States";
    private static final String ROW1_TIME_ZONE = "EST";

    private static final String ROW2_COUNTRY_ISO2_CODE = "GB";
    private static final String ROW2_SWIFT_CODE = "EFGHGB2L";
    private static final String ROW2_CODE_TYPE = "TYPE2";
    private static final String ROW2_NAME = "Another Bank";
    private static final String ROW2_ADDRESS = "5678 Avenue";
    private static final String ROW2_TOWN_NAME = "London";
    private static final String ROW2_COUNTRY_NAME = "United Kingdom";
    private static final String ROW2_TIME_ZONE = "GMT";

    private static final String CSV_HEADER = String.join(",",
            COL_COUNTRY_ISO2_CODE,
            COL_SWIFT_CODE,
            COL_CODE_TYPE,
            COL_NAME,
            COL_ADDRESS,
            COL_TOWN_NAME,
            COL_COUNTRY_NAME,
            COL_TIME_ZONE);

    private static final String CSV_ROW_1 = String.join(",",
            ROW1_COUNTRY_ISO2_CODE,
            ROW1_SWIFT_CODE,
            ROW1_CODE_TYPE,
            ROW1_NAME,
            ROW1_ADDRESS,
            ROW1_TOWN_NAME,
            ROW1_COUNTRY_NAME,
            ROW1_TIME_ZONE);

    private static final String CSV_ROW_2 = String.join(",",
            ROW2_COUNTRY_ISO2_CODE,
            ROW2_SWIFT_CODE,
            ROW2_CODE_TYPE,
            ROW2_NAME,
            ROW2_ADDRESS,
            ROW2_TOWN_NAME,
            ROW2_COUNTRY_NAME,
            ROW2_TIME_ZONE);

    private static final String CSV_DATA = CSV_HEADER + "\n" + CSV_ROW_1 + "\n" + CSV_ROW_2;

    private CsvService csvService;

    @BeforeEach
    public void setUp() {
        csvService = new CsvService();
    }

    /**
     * Verifies that a well-formed CSV with two records is parsed correctly.
     */
    @Test
    public void testReadCsv_forSwiftDataCsv() {
        Resource resource = new ByteArrayResource(CSV_DATA.getBytes(StandardCharsets.UTF_8));
        List<SwiftDataCsv> records = csvService.readCsv(SwiftDataCsv.class, resource);

        Assertions.assertNotNull(records, "Must not be null");
        Assertions.assertEquals(2, records.size(), "Expected 2 records");

        SwiftDataCsv firstRecord = records.get(0);
        Assertions.assertEquals(ROW1_COUNTRY_ISO2_CODE, firstRecord.getCountryISO2Code());
        Assertions.assertEquals(ROW1_SWIFT_CODE, firstRecord.getSwiftCode());
        Assertions.assertEquals(ROW1_CODE_TYPE, firstRecord.getCodeType());
        Assertions.assertEquals(ROW1_NAME, firstRecord.getName());
        Assertions.assertEquals(ROW1_ADDRESS, firstRecord.getAddress());
        Assertions.assertEquals(ROW1_TOWN_NAME, firstRecord.getTownName());
        Assertions.assertEquals(ROW1_COUNTRY_NAME, firstRecord.getCountryName());
        Assertions.assertEquals(ROW1_TIME_ZONE, firstRecord.getTimeZone());

        SwiftDataCsv secondRecord = records.get(1);
        Assertions.assertEquals(ROW2_COUNTRY_ISO2_CODE, secondRecord.getCountryISO2Code());
        Assertions.assertEquals(ROW2_SWIFT_CODE, secondRecord.getSwiftCode());
        Assertions.assertEquals(ROW2_CODE_TYPE, secondRecord.getCodeType());
        Assertions.assertEquals(ROW2_NAME, secondRecord.getName());
        Assertions.assertEquals(ROW2_ADDRESS, secondRecord.getAddress());
        Assertions.assertEquals(ROW2_TOWN_NAME, secondRecord.getTownName());
        Assertions.assertEquals(ROW2_COUNTRY_NAME, secondRecord.getCountryName());
        Assertions.assertEquals(ROW2_TIME_ZONE, secondRecord.getTimeZone());
    }

    /**
     * Verifies that an empty CSV returns an empty list.
     */
    @Test
    public void testReadCsv_emptyCsv_returnsEmptyList() {
        String emptyCsv = "";
        Resource resource = new ByteArrayResource(emptyCsv.getBytes(StandardCharsets.UTF_8));
        List<SwiftDataCsv> records = csvService.readCsv(SwiftDataCsv.class, resource);

        Assertions.assertNotNull(records, "List must not be null");
        Assertions.assertTrue(records.isEmpty(), "List must be empty");
    }

    /**
     * Verifies that a malformed CSV triggers a CsvParsingException.
     */
    @Test
    public void testReadCsv_malformedCsv_throwsException() {
        String malformedCsv = CSV_HEADER + "\n" +
                ROW1_COUNTRY_ISO2_CODE + "," +
                ROW1_SWIFT_CODE + "," +
                ROW1_CODE_TYPE + ",\"" +
                ROW1_NAME + "," +
                ROW1_ADDRESS + "," +
                ROW1_TOWN_NAME + "," +
                ROW1_COUNTRY_NAME + "," +
                ROW1_TIME_ZONE;
        Resource resource = new ByteArrayResource(malformedCsv.getBytes(StandardCharsets.UTF_8));

        Assertions.assertThrows(CsvParsingException.class, () -> {
            csvService.readCsv(SwiftDataCsv.class, resource);
        }, "Should cause CsvParsingException");
    }
}
