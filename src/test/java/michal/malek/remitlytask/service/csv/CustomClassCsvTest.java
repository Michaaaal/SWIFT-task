package michal.malek.remitlytask.service.csv;

import michal.malek.remitlytask.model.CustomCsvRecord;
import michal.malek.remitlytask.exception.CsvParsingException;
import michal.malek.remitlytask.service.CsvService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Tests CSV parsing for custom CSV records.
 */
public class CustomClassCsvTest {

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_VALUE = "VALUE";

    private static final String FIRST_RECORD_ID = "1";
    private static final String FIRST_RECORD_NAME = "Record One";
    private static final String FIRST_RECORD_VALUE = "123.45";

    private static final String SECOND_RECORD_ID = "2";
    private static final String SECOND_RECORD_NAME = "Record Two";
    private static final String SECOND_RECORD_VALUE = "678.90";

    private static final String CSV_HEADER = String.join(",", COL_ID, COL_NAME, COL_VALUE);
    private static final String CSV_ROW_1 = String.join(",", FIRST_RECORD_ID, FIRST_RECORD_NAME, FIRST_RECORD_VALUE);
    private static final String CSV_ROW_2 = String.join(",", SECOND_RECORD_ID, SECOND_RECORD_NAME, SECOND_RECORD_VALUE);
    private static final String CSV_DATA = CSV_HEADER + "\n" + CSV_ROW_1 + "\n" + CSV_ROW_2;

    private CsvService csvService;

    @BeforeEach
    public void setUp() {
        csvService = new CsvService();
    }

    /**
     * Valid CSV data is parsed into records correctly.
     */
    @Test
    public void testReadCsv_forCustomCsvRecord() {
        Resource resource = new ByteArrayResource(CSV_DATA.getBytes(StandardCharsets.UTF_8));
        List<CustomCsvRecord> records = csvService.readCsv(CustomCsvRecord.class, resource);

        Assertions.assertNotNull(records, "Record list should not be null");
        Assertions.assertEquals(2, records.size(), "Expected two records");

        CustomCsvRecord firstRecord = records.get(0);
        Assertions.assertEquals(1, firstRecord.getId(), "First record ID should be 1");
        Assertions.assertEquals(FIRST_RECORD_NAME, firstRecord.getName(), "First record name mismatch");
        Assertions.assertEquals(123.45, firstRecord.getValue(), 0.001, "First record value mismatch");

        CustomCsvRecord secondRecord = records.get(1);
        Assertions.assertEquals(2, secondRecord.getId(), "Second record ID should be 2");
        Assertions.assertEquals(SECOND_RECORD_NAME, secondRecord.getName(), "Second record name mismatch");
        Assertions.assertEquals(678.90, secondRecord.getValue(), 0.001, "Second record value mismatch");
    }

    /**
     * Malformed CSV data throws CsvParsingException.
     */
    @Test
    public void testReadCsv_forCustomCsvRecord_malformedCsv_throwsException() {
        String malformedCsv = CSV_HEADER + "\n" + FIRST_RECORD_ID + "," + FIRST_RECORD_NAME;
        Resource resource = new ByteArrayResource(malformedCsv.getBytes(StandardCharsets.UTF_8));

        Assertions.assertThrows(CsvParsingException.class, () -> {
            csvService.readCsv(CustomCsvRecord.class, resource);
        }, "Should throw CsvParsingException");
    }
}
