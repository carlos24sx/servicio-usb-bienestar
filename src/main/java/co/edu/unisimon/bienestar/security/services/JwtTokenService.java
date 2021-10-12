package co.edu.unisimon.bienestar.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import co.edu.unisimon.bienestar.security.configuration.JwtConfiguration;

import javax.crypto.SecretKey;

/**
 * @author William Torres
 * @version 1.0.0
 */

@Component
@AllArgsConstructor
public class JwtTokenService {

    private final JwtConfiguration jwtConfiguration;

    /**
     * @return SecretKey. Encrypt the 'signature' of the token.
     * @apiNote The purpose is to ensure that the message has not been altered and to recognize the signer to validate the JWT.
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecret().getBytes());
    }

    /**
     * @return Claims of access token.
     * @apiNote The claims save information of token and application user. JWT allow claims to be represented in a secure manner.
     */
    public Claims getClaims(String authorization) {
        String token = authorization.replace(jwtConfiguration.getPrefix(), "");
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
