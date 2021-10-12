package co.edu.unisimon.bienestar.common;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import co.edu.unisimon.bienestar.security.configuration.JwtConfiguration;
import co.edu.unisimon.bienestar.security.services.JwtTokenService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author William Torres
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class TokenUtil {

    private final JwtTokenService jwtTokenService;
    private final JwtConfiguration jwtConfiguration;

    private String getHeader(HttpServletRequest request, String header) {
        return request.getHeader(header);
    }

    private String getAuthorization(HttpServletRequest request) {
        return getHeader(request, jwtConfiguration.getHeader());
    }

    public String getPropertyFromToken(HttpServletRequest request, String property) {
        String authorization = getAuthorization(request);
        Claims claims = jwtTokenService.getClaims(authorization);
        return String.valueOf(claims.get(property));
    }
    
    //Obtener Id estudiante
    
    public Long getUsuarioId(HttpServletRequest request) {
        return Long.valueOf(
                getPropertyFromToken(request, "student_id")
        );
    }
    
    //Obtener Id Sede
    public Integer getSede(HttpServletRequest request) {
        return Integer.valueOf(
                getPropertyFromToken(request, "place_id")
        );
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals(role)
        );
    }

    public String getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

}
