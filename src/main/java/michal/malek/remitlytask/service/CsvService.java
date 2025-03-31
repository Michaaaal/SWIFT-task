package michal.malek.remitlytask.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import michal.malek.remitlytask.exception.CsvParsingException;
import michal.malek.remitlytask.model.constant.messages.ExceptionMessage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;

/**
 * Service responsible for reading data from csv.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CsvService {

    /**
     * Provides flexible possibilities for reading csv files.
     * Task does not require flexible solution, but I wanted to implement
     * flexible solution for reading csv files.
     * @param csvClass Describes which model is read.
     * @param resource Provides information about read csv.
     * @return List containing read information.
     * @param <T> Allows to read different csv files.
     */
    public <T> List<T> readCsv(Class<T> csvClass, Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(csvClass)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            String message = String.format(ExceptionMessage.CSV_READ_FAILURE, csvClass.getName() , new Date());
            log.error(message);
            throw new CsvParsingException(message, e);
        } catch (Exception e) {
            String message = String.format(ExceptionMessage.CSV_PROCESSING_FAILURE, csvClass.getName() , new Date());
            log.error(message);
            throw new CsvParsingException(message, e);
        }
    }
}
