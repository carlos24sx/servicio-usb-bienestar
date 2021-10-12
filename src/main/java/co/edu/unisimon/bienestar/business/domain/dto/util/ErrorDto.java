package co.edu.unisimon.bienestar.business.domain.dto.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "create")
public class ErrorDto implements Serializable {

    private static final long serialVersionUID = 7074808751192877967L;

    /**
     * The HTTP status code
     */
    @NonNull
    private Integer status;
    /**
     * The error message associated with exception
     */
    @NonNull
    private String message;
    /**
     * List of constructed error messages
     */
    @NonNull
    private String details;
    /**
     * Date and time the exception occurred
     */
    private LocalDateTime timestamp = LocalDateTime.now();

}
