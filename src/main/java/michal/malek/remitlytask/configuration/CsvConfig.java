package michal.malek.remitlytask.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


/**
 * Configuration class for CSV file resource.
 * Reads the CSV file path from application properties and provides a Resource bean
 * for accessing the CSV file located on the classpath.
 */
@Configuration
public class CsvConfig {

    /**
     * Injects the CSV file path from the configuration.
     * @param swiftCsvPath the CSV file path from properties
     */
    public CsvConfig(@Value("${swift.csv.path}") String swiftCsvPath) {
        this.swiftCsvPath = swiftCsvPath;
    }

    /**
     * Holds the path to the CSV file, injected from application properties.
     */
    private final String swiftCsvPath;

    /**
     * Creates a Resource bean pointing to the CSV file.
     * @return the Resource representing CSV file
     */
    @Bean
    public Resource resource() {
        return new ClassPathResource(swiftCsvPath);
    }
}

