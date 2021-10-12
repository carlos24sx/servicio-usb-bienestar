package co.edu.unisimon.bienestar.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author William Torres
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 7990520604551351034L;

    public ForbiddenException(String message) {
        super(message);
    }

}
