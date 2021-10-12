package co.edu.unisimon.bienestar.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author William Torres
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 7509740595618586822L;

    public BadRequestException(String message) {
        super(message);
    }

}
