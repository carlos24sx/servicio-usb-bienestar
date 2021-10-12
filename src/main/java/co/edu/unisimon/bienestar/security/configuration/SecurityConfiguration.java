package co.edu.unisimon.bienestar.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


/**
 * @author William Torres
 * @version 1.0.0
 * @apiNote Initialize the necessary beans in the security scheme.
 */

@Configuration
public class SecurityConfiguration {

    /**
     * @return Implementation of PasswordEncoder that uses the BCrypt strong hashing function. Clients can optionally
     * supply a "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter
     * the more work will have to be done (exponentially) to hash the passwords. The default value is 10.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Interface to be implemented by classes (usually HTTP request handlers) that provides a CorsConfiguration instance based on the provided request.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "OPTIONS", "DELETE"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(false);
        configuration.setExposedHeaders(Collections.singletonList("Content-Disposition"));
        configuration.setMaxAge(4800L);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
