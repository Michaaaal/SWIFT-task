package michal.malek.remitlytask.model.standard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Class for exception response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StandardizedExceptionResponse {
    private String message;
    private int status;
    private Date timestamp;

    public StandardizedExceptionResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
        this.timestamp = new Date();
    }
}
