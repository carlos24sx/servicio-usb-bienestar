package co.edu.unisimon.bienestar.business.domain.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Getter
@Component
public class Message implements Serializable {

    private static final long serialVersionUID = 1652359669445648982L;

    public static final String FIELD = "$FIELD";
    public static final String ENTITY = "$ENTITY";
    public static final String FIELDS = "$FIELDS";

    /**
     * Mensaje general en caso de presentarse una excepción no controlada.
     */
    @Value("${message.exception.general}")
    private String general;

    @Value("${message.exception.unauthorized}")
    private String unauthorized;

    @Value("${message.exception.forbidden}")
    private String forbidden;

    @Value("${message.exception.not-found}")
    private String noFound;

    @Value("${message.exception.inconsistency}")
    private String inconsistency;

    @Value("${message.exception.bad-request}")
    private String badRequest;

    @Value("${message.exception.method-not-allowed}")
    private String methodNoAllow;

    @Value("${message.exception.missing-servlet-request-parameter}")
    private String missingServletRequestParameter;

    @Value("${message.exception.constraint-violation}")
    private String constraintViolation;

    @Value("${message.exception.constraint-violation}")
    private String argumentTypeMismatch;

    @Value("${message.exception.media-type-not-supported}")
    private String mediaTypeNoSupported;

    @Value("${message.exception.custom.no-data}")
    private String customNoData;

    @Value("${message.exception.custom.null-field}")
    private String nullField;

    @Value("${message.exception.custom.no-null-field}")
    private String noNullField;

    @Value("${message.exception.custom.exist-data}")
    private String customExistData;

    public String getMessage(String message, Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String parameter = entry.getKey();
            String value = entry.getValue();
            message = message.replace(parameter, value);
        }
        return message;
    }

}
