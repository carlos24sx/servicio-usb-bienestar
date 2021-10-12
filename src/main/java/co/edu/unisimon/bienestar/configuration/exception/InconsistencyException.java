package co.edu.unisimon.bienestar.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author William Torres
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InconsistencyException extends RuntimeException {

    private static final long serialVersionUID = -8158577423716598672L;

    public InconsistencyException(String message) {
        super(message);
    }

}
