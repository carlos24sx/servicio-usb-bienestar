package co.edu.unisimon.bienestar.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author William Torres
 * @version 1.0
 */
@Slf4j
public final class Logger {

    private Logger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Permite realizar un seguimiento de las acciones realizadas por un usuario
     *
     * @param request Petición HTTP/HTTPPS
     */
    public static void tracing(HttpServletRequest request) {
        String username = Objects.nonNull(request.getUserPrincipal()) ? request.getUserPrincipal().getName() : "default-user";
        String uri = request.getRequestURI();
        String methodHttp = request.getMethod();
        String ip = request.getRemoteAddr();
        log.trace("El usuario ".concat(username).concat(" desde la IP ").concat(ip)
                .concat(" accede a ").concat(uri).concat(" a través del método ")
                .concat(methodHttp));
    }


}
