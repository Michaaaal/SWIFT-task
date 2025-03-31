package michal.malek.remitlytask.service.swift_data;

import michal.malek.remitlytask.exception.SwiftDataAlreadyExistsException;
import michal.malek.remitlytask.exception.SwiftDataNotFoundException;
import michal.malek.remitlytask.exception.SwiftDataNotValidException;
import michal.malek.remitlytask.mapper.SwiftDataMapper;
import michal.malek.remitlytask.model.standard.StandardizedSuccessResponse;
import michal.malek.remitlytask.model.swift_data.SwiftDataDoc;
import michal.malek.remitlytask.model.swift_data.request.SwiftDataRequest;
import michal.malek.remitlytask.model.swift_data.response.BranchResponse;
import michal.malek.remitlytask.model.swift_data.response.CountrySwiftDataResponse;
import michal.malek.remitlytask.model.swift_data.response.HeadquarterAndBranchesResponse;
import michal.malek.remitlytask.repository.SwiftDataRepository;
import michal.malek.remitlytask.service.SwiftDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwiftDataServiceTest {
    @Mock private SwiftDataRepository swiftDataRepository;
    @Mock private SwiftDataMapper swiftDataMapper;

    @InjectMocks private SwiftDataService swiftDataService;

    private static final String HEADQUARTERS_SWIFT_CODE = "ABCDEFGHXXX";
    private static final String NON_HEADQUARTERS_SWIFT_CODE = "BRANCH001";
    private static final String COUNTRY_ISO2 = "US";

    private SwiftDataDoc headquartersDoc;
    private SwiftDataDoc branchDoc;
    private SwiftDataRequest validRequest;
    private SwiftDataRequest invalidRequestWrongXXX;
    private SwiftDataRequest invalidRequestDuplicate;

    @BeforeEach
    public void setUpTestData() {
        headquartersDoc = new SwiftDataDoc();
        headquartersDoc.setSwiftCode(HEADQUARTERS_SWIFT_CODE);
        headquartersDoc.setHeadquarter(true);
        headquartersDoc.setCountryISO2(COUNTRY_ISO2);
        headquartersDoc.setCountryName("United States");

        branchDoc = new SwiftDataDoc();
        branchDoc.setSwiftCode("ABCDEFGH");
        branchDoc.setHeadquarter(false);
        branchDoc.setCountryISO2(COUNTRY_ISO2);
        branchDoc.setCountryName("United States");

        validRequest = new SwiftDataRequest();
        validRequest.setSwiftCode(HEADQUARTERS_SWIFT_CODE);
        validRequest.setIsHeadquarter(true);

        invalidRequestWrongXXX = new SwiftDataRequest();
        invalidRequestWrongXXX.setSwiftCode(HEADQUARTERS_SWIFT_CODE);
        invalidRequestWrongXXX.setIsHeadquarter(false);

        invalidRequestDuplicate = new SwiftDataRequest();
        invalidRequestDuplicate.setSwiftCode("XYZ123XXX");
        invalidRequestDuplicate.setIsHeadquarter(true);
    }

    /**
     * Test getSwiftDataWithBranches for a headquarters record.
     * Expects branch list to be set in the returned HeadquarterAndBranchesResponse.
     */
    @Test
    public void testGetSwiftDataWithBranches_whenHeadquarter_returnsBranches() {
        when(swiftDataRepository.findById(HEADQUARTERS_SWIFT_CODE))
                .thenReturn(Optional.of(headquartersDoc));

        HeadquarterAndBranchesResponse hqResponse = new HeadquarterAndBranchesResponse();
        when(swiftDataMapper.swiftDocToHeadquarter(headquartersDoc)).thenReturn(hqResponse);

        when(swiftDataRepository.findBySwiftCodeStartingWithAndIsHeadquarterFalse(anyString()))
                .thenReturn(Collections.singletonList(branchDoc));
        BranchResponse branchResponse = new BranchResponse(); // dummy branch response
        when(swiftDataMapper.swiftDocListToResponseList(Collections.singletonList(branchDoc)))
                .thenReturn(List.of(branchResponse));

        HeadquarterAndBranchesResponse result = swiftDataService.getSwiftDataWithBranches(HEADQUARTERS_SWIFT_CODE);

        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getBranches(), "Branches should not be null for a headquarters record");
        assertEquals(1, result.getBranches().size(), "Expected one branch");
    }

    /**
     * Test getSwiftDataWithBranches for a non-headquarters record.
     * Expects no branches to be set.
     */
    @Test
    public void testGetSwiftDataWithBranches_whenNotHeadquarter_returnsResponseWithoutBranches() {
        SwiftDataDoc nonHqDoc = new SwiftDataDoc();
        nonHqDoc.setSwiftCode(NON_HEADQUARTERS_SWIFT_CODE);
        nonHqDoc.setHeadquarter(false);
        when(swiftDataRepository.findById(NON_HEADQUARTERS_SWIFT_CODE))
                .thenReturn(Optional.of(nonHqDoc));
        HeadquarterAndBranchesResponse response = new HeadquarterAndBranchesResponse();
        when(swiftDataMapper.swiftDocToHeadquarter(nonHqDoc)).thenReturn(response);

        HeadquarterAndBranchesResponse result = swiftDataService.getSwiftDataWithBranches(NON_HEADQUARTERS_SWIFT_CODE);

        assertNotNull(result, "Result should not be null");
        assertNull(result.getBranches(), "Branches should be null for a non-headquarters record");
    }

    /**
     * Test getCountrySwiftData when data is found.
     */
    @Test
    public void testGetCountrySwiftData_whenDataFound_returnsResponse() {
        when(swiftDataRepository.findByCountryISO2(COUNTRY_ISO2))
                .thenReturn(Collections.singletonList(headquartersDoc));
        List<BranchResponse> dummyResponses = List.of(new BranchResponse());
        when(swiftDataMapper.swiftDocListToResponseList(anyList()))
                .thenReturn(dummyResponses);

        CountrySwiftDataResponse result = swiftDataService.getCountrySwiftData(COUNTRY_ISO2);

        assertNotNull(result, "Result should not be null");
        assertEquals(COUNTRY_ISO2, result.getCountryISO2(), "Country ISO2 should match");
        assertEquals("United States", result.getCountryName(), "Country name should match");
        assertEquals(dummyResponses, result.getSwiftCodes(), "Swift codes list should match");
    }

    /**
     * Test getCountrySwiftData when no data is found.
     * Expects SwiftDataNotFoundException.
     */
    @Test
    public void testGetCountrySwiftData_whenDataNotFound_throwsException() {
        when(swiftDataRepository.findByCountryISO2(COUNTRY_ISO2))
                .thenReturn(Collections.emptyList());

        Exception ex = assertThrows(SwiftDataNotFoundException.class, () ->
                swiftDataService.getCountrySwiftData(COUNTRY_ISO2)
        );
        assertTrue(ex.getMessage().contains(COUNTRY_ISO2));
    }

    /**
     * Test addSwiftData with a valid request.
     */
    @Test
    public void testAddSwiftData_success() {
        when(swiftDataRepository.existsById(validRequest.getSwiftCode())).thenReturn(false);
        SwiftDataDoc doc = new SwiftDataDoc();
        doc.setSwiftCode(validRequest.getSwiftCode());
        when(swiftDataMapper.swiftRequestToDoc(validRequest)).thenReturn(doc);
        when(swiftDataRepository.save(doc)).thenReturn(doc);

        StandardizedSuccessResponse response = swiftDataService.addSwiftData(validRequest);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.getMessage().contains(validRequest.getSwiftCode()));
    }

    /**
     * Test addSwiftData when the request has invalid XXX usage.
     */
    @Test
    public void testAddSwiftData_invalidRequest_wrongXXXUsage_throwsException() {
        Exception ex = assertThrows(SwiftDataNotValidException.class, () ->
                swiftDataService.addSwiftData(invalidRequestWrongXXX)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("xxx"), "Exception message should mention XXX usage");
    }

    /**
     * Test addSwiftData when a duplicate swift code exists.
     */
    @Test
    public void testAddSwiftData_duplicateSwiftCode_throwsException() {
        when(swiftDataRepository.existsById(invalidRequestDuplicate.getSwiftCode())).thenReturn(true);

        Exception ex = assertThrows(SwiftDataAlreadyExistsException.class, () ->
                swiftDataService.addSwiftData(invalidRequestDuplicate)
        );
        assertTrue(ex.getMessage().contains(invalidRequestDuplicate.getSwiftCode()));
    }

    /**
     * Test deleteSwiftData for a successful deletion.
     */
    @Test
    public void testDeleteSwiftData_success() {
        String swiftCode = "TESTCODE";
        when(swiftDataRepository.existsById(swiftCode)).thenReturn(true);

        StandardizedSuccessResponse response = swiftDataService.deleteSwiftData(swiftCode);

        verify(swiftDataRepository, times(1)).deleteById(swiftCode);
        assertTrue(response.getMessage().contains(swiftCode));
    }

    /**
     * Test deleteSwiftData when the swift code does not exist.
     */
    @Test
    public void testDeleteSwiftData_notFound_throwsException() {
        String swiftCode = "NON_EXISTENT";
        when(swiftDataRepository.existsById(swiftCode)).thenReturn(false);

        Exception ex = assertThrows(SwiftDataNotFoundException.class, () ->
                swiftDataService.deleteSwiftData(swiftCode)
        );
        assertTrue(ex.getMessage().contains(swiftCode));
    }
}
