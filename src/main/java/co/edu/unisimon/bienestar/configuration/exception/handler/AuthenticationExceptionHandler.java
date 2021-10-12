package co.edu.unisimon.bienestar.configuration.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unisimon.bienestar.business.domain.constant.Message;
import co.edu.unisimon.bienestar.business.domain.dto.util.ErrorDto;
import co.edu.unisimon.bienestar.business.domain.dto.util.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author William Torres
 * @version 1.0.0
 * @apiNote Responsible for overwriting the response generated by Spring when occurs a AuthenticationException (Classic Error HTTP - 401).
 * <p>
 * Interfaces:
 * - AuthenticationEntryPoint: Used by ExceptionTranslationFilter to commence an authentication scheme.
 * <p>
 * Annotations(@):
 * - Component: Indicates that an annotated class is a "component". Such classes are considered as candidates for auto-detection when using annotation-based configuration and classpath scanning.
 */
@Component
@AllArgsConstructor
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -5454336529303954168L;

    private final Message message;
    private final ObjectMapper objectMapper;

    /**
     * Commences an authentication scheme.
     * Note: A ResponseDto object is sent instead of the classic response.
     **/
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseDto<ErrorDto> responseDto = ResponseDto.create(
                ErrorDto.create(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        message.getUnauthorized(),
                        "Se requiere autenticación completa para acceder a este recurso."
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(responseDto));
    }

}