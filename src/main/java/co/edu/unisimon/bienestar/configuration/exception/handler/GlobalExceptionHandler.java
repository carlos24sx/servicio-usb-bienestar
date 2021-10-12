package co.edu.unisimon.bienestar.configuration.exception.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.edu.unisimon.bienestar.business.domain.constant.Message;
import co.edu.unisimon.bienestar.business.domain.dto.util.ErrorDto;
import co.edu.unisimon.bienestar.business.domain.dto.util.ResponseDto;
import co.edu.unisimon.bienestar.common.MapUtil;
import co.edu.unisimon.bienestar.configuration.exception.BadRequestException;
import co.edu.unisimon.bienestar.configuration.exception.ForbiddenException;
import co.edu.unisimon.bienestar.configuration.exception.InconsistencyException;
import co.edu.unisimon.bienestar.configuration.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author William Torres
 * @version 1.0
 */
@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Allows access to the configuration parameters defined in the database.properties file.
     */
    private final Environment env;
    private final Message message;

    private static final String PACKAGE = "unisimon";
    private static final String TXT_ERROR = "ERROR - { ";
    private static final String TXT_REASON = " REASON: ";
    private static final String TXT_DETAILS = ". Más información: ";

    /**
     * Get data about exception
     */
    private Map<String, String> getDataFromException(Exception exception) {
        String clase = exception.getClass().getSimpleName();
        String mensaje = (exception.getMessage() != null) ? exception.getMessage() : "No se identificó un mensaje";
        String cause = (exception.getCause() != null && exception.getCause().getCause() != null) ? exception.getCause().getCause().getMessage() : "No se identificó una causa";
        String detalle = "";
        if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
            StackTraceElement[] stackTrace = exception.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (stackTraceElement.getClassName().contains(PACKAGE)) {
                    detalle = stackTraceElement.toString();
                    break;
                }
            }
        }
        return MapUtil.getMap("class", clase, "message", mensaje, "cause", cause, "details", detalle);
    }

    public String processException(Exception e) {
        String messageException = "";
        String profile = env.getProperty("spring.profiles.active");
        if ("dev".equalsIgnoreCase(profile)) {
            Map<String, String> dataException = getDataFromException(e);
            messageException = "Clase: ".concat(dataException.get("class"))
                    .concat(". Causa: ").concat(dataException.get("cause"))
                    .concat(". Mensaje: ").concat(dataException.get("message"))
                    .concat(". Detalles: ").concat(dataException.get("details"));
        }
        return messageException;
    }

    /**
     * Build the response to handling exceptions
     */
    private ResponseEntity<ResponseDto<ErrorDto>> response(ErrorDto error, HttpStatus httpStatus) {
        return new ResponseEntity<>(ResponseDto.create(error), httpStatus);
    }

    /*
     * ==================
     * Project exceptions
     * ==================
     */

    /**
     * This exception is thrown when a business resource wasn't found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto<ErrorDto>> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        String messageException = e.getMessage();
        log.warn(TXT_ERROR.concat(request.getMethod()).concat(" } ").concat(request.getRequestURI()).concat(TXT_REASON).concat(messageException));
        return response(ErrorDto.create(HttpServletResponse.SC_NOT_FOUND, message.getNoFound(), messageException), HttpStatus.NOT_FOUND);
    }

    /**
     * This exception is thrown when a user has not permission to do an action or access a resource
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto<ErrorDto>> handleForbiddenException(ForbiddenException e, HttpServletRequest request) {
        String messageException = e.getMessage();
        log.warn(TXT_ERROR.concat(request.getMethod()).concat(" } ").concat(request.getRequestURI()).concat(TXT_REASON).concat(messageException));
        return response(ErrorDto.create(HttpServletResponse.SC_FORBIDDEN, message.getForbidden(), messageException), HttpStatus.FORBIDDEN);
    }

    /**
     * This exception is thrown when the data of a request is incorrect.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto<ErrorDto>> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        String messageException = e.getMessage();
        log.warn(TXT_ERROR.concat(request.getMethod()).concat(" } ").concat(request.getRequestURI()).concat(TXT_REASON).concat(messageException));
        return response(ErrorDto.create(HttpServletResponse.SC_BAD_REQUEST, message.getBadRequest(), messageException), HttpStatus.BAD_REQUEST);
    }

    /**
     * This exception occurs when there is an inconsistency in the data in the database.
     */
    @ExceptionHandler(InconsistencyException.class)
    public ResponseEntity<ResponseDto<ErrorDto>> handleInconsistencyException(InconsistencyException e, HttpServletRequest request) {
        String messageException = e.getMessage();
        log.warn(TXT_ERROR.concat(request.getMethod()).concat(" } ").concat(request.getRequestURI()).concat(TXT_REASON).concat(messageException));
        return response(ErrorDto.create(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message.getInconsistency(), messageException), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
     * ==================
     * Spring exceptions
     * ==================
     */

    /**
     * MissingServletRequestPartException: This exception is thrown when when the part of a multipart request not found.
     * MissingServletRequestParameterException: This exception is thrown when request missing parameter.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "El parámetro ".concat(e.getParameterName()).concat(" es requerido.");
        String messageException = error.concat(TXT_DETAILS).concat(Objects.requireNonNull(e.getMessage()));
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(status.value(), message.getMissingServletRequestParameter(), error)),
                status
        );
    }

    /**
     * ConstrainViolationException: This exception reports the result of constraint violations.
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        String messageException = processException(e);
        StringBuilder errors = new StringBuilder();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.append(
                    violation.getRootBeanClass().getName()
                            .concat(" ")
                            .concat(violation.getPropertyPath().toString())
                            .concat(": ").concat(violation.getMessage())
                            .concat(" "));
        }
        messageException = messageException.concat(TXT_DETAILS).concat(errors.toString());
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return response(ErrorDto.create(HttpServletResponse.SC_BAD_REQUEST, message.getConstraintViolation(), messageException), HttpStatus.BAD_REQUEST);
    }

    /**
     * DataIntegrityViolationException: Exception thrown when an attempt to insert or update data results in violation of an integrity constraint.
     * Note that this is not purely a relational concept; unique primary keys are required by most database types.
     */
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        String messageException = processException(e);
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException != null ? messageException : ""));
        return response(ErrorDto.create(HttpServletResponse.SC_BAD_REQUEST, message.getConstraintViolation(), messageException), HttpStatus.BAD_REQUEST);
    }

    /**
     * MethodArgumentTypeMismatchException: This exception is thrown when method argument is not the expected type
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String error = "El parámetro ".concat(e.getName()).concat(" debe ser de tipo ").concat((e.getRequiredType() != null ? e.getRequiredType().getName() : ""));
        String messageException = error.concat(TXT_DETAILS).concat(e.getMessage() != null ? e.getMessage() : "");
        log.warn(TXT_ERROR.concat(request.getMethod()).concat(" } ").concat(request.getRequestURI()).concat(TXT_REASON).concat(messageException));
        return response(ErrorDto.create(HttpServletResponse.SC_BAD_REQUEST, message.getArgumentTypeMismatch(), error), HttpStatus.BAD_REQUEST);
    }

    /**
     * MissingPathVariableException: {@link ServletRequestBindingException} subclass that indicates that a path
     * variable expected in the method parameters of an {@code @RequestMapping}
     * method is not present among the URI variables extracted from the URL.
     * Typically that means the URI template does not match the path variable name
     * declared on the method parameter.
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messageException = e.getCause() != null && e.getCause().getMessage() != null ? e.getCause().getMessage() : "";
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(HttpServletResponse.SC_BAD_REQUEST, message.getBadRequest(), messageException)),
                status
        );
    }

    /**
     * HttpRequestMethodNotSupportedException: This exception is thrown when you send a requested with an unsupported HTTP method
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder errors = new StringBuilder();
        errors.append(" El método ");
        errors.append(e.getMethod());
        errors.append(" no es soportado para esta petición. Los métodos soportados son ");
        if (e.getSupportedHttpMethods() != null) {
            e.getSupportedHttpMethods().forEach(stm -> errors.append(stm).append(" "));
        }
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(errors.toString()));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(HttpServletResponse.SC_METHOD_NOT_ALLOWED, message.getMethodNoAllow(), errors.toString())), status
        );
    }

    /**
     * HttpMediaTypeNotSupportedException: Occurs when the client send a request with unsupported media type.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder error = new StringBuilder();
        error.append("El tipo de media ");
        error.append(e.getContentType());
        error.append(" no es soportado. Los tipos de media soportados son ");
        e.getSupportedMediaTypes().forEach(smt -> error.append(smt).append(" "));
        String messageException = error.toString().concat(TXT_DETAILS).concat(error.toString());
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, message.getMediaTypeNoSupported(), error.toString())),
                status
        );
    }

    /**
     * MethodArgumentNotValidException: This exception is thrown when argument annotated with @Valid failed validation:
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messageException = processException(e);

        List<String> errorList = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorList.add(error.getField() + ": " + error.getDefaultMessage());
        }
        Optional<String> optionalErrors = errorList.stream().reduce((prev, next) -> prev.concat(", ").concat(next));
        messageException = messageException.concat(TXT_DETAILS).concat(optionalErrors.orElse(""));
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(
                        HttpServletResponse.SC_BAD_REQUEST,
                        message.getBadRequest(),
                        optionalErrors.orElse("")
                )),
                status
        );
    }

    /**
     * We can customize our servlet to throw this exception instead of send 404 response. See main method (in the class with @SpringBootApplication).
     */
    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messageException = "No fue posible manejar la petición ".concat(e.getRequestURL()).concat(" mediante el método ").concat(e.getHttpMethod());
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(HttpServletResponse.SC_NOT_FOUND, message.getNoFound(), messageException)),
                status
        );
    }

    /**
     * Thrown by HttpMessageConverter implementations when the HttpMessageConverter.read(java.lang.Class<? extends T>, org.springframework.http.HttpInputMessage) method fails.
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messageException = processException(e);
        log.warn(TXT_ERROR.concat(request.getDescription(true)).concat(" } ").concat(TXT_REASON).concat(messageException));
        return new ResponseEntity<>(
                ResponseDto.create(ErrorDto.create(
                        HttpServletResponse.SC_BAD_REQUEST,
                        message.getBadRequest(),
                        !messageException.isEmpty() ? messageException : "El cupero de la petición no puede ser procesado."
                )),
                status
        );
    }

    /*
     * ==================
     * General exceptions
     * ==================
     */

    /**
     * Finally, let’s implement a fall-back handler – a catch-all type of logic that deals with all other exceptions that don’t have specific handlers.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseDto<ErrorDto>> handleAllExceptions(Exception e, HttpServletRequest request) {
        String messageException = processException(e);
        log.error(TXT_ERROR.concat(request.getMethod()).concat(" } ").concat(request.getRequestURI()).concat(TXT_REASON).concat(messageException));
        log.error("Extensión de la excepción: ", e);
        return response(ErrorDto.create(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message.getGeneral(), messageException), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
