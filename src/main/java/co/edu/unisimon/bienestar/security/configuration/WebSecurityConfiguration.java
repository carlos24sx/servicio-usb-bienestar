package co.edu.unisimon.bienestar.security.configuration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import co.edu.unisimon.bienestar.configuration.exception.handler.AuthenticationExceptionHandler;
import co.edu.unisimon.bienestar.security.filters.JwtAuthorizationFilter;
import co.edu.unisimon.bienestar.security.services.JwtTokenService;

/**
 * @author Willliam Torres
 * @version 1.0
 */
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenService jwtTokenService;
    private final JwtConfiguration jwtConfiguration;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                /*
                 * Make sure we use stateless session; session won't be used to store user's state.
                 */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /*
                 * Handle unauthorized attempts
                 * The 'authenticationEntryPoint' that overwrites the Spring response for error 401 is injected
                 */
                .exceptionHandling().authenticationEntryPoint(authenticationExceptionHandler)
                .and()
                /*
                 * Add a filter to validate user credentials and add token in the response header
                 * What's the authenticationManager()? An object provided by WebSecurityConfigurerAdapter, used to authenticate
                 * the user passing user's credentials.The filter needs this auth manager to authenticate the user.
                 */
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtConfiguration, jwtTokenService))
                .authorizeRequests()
                /*
                 * Requests that the authorization filter will ignore are set
                 */
                .antMatchers(HttpMethod.GET, jwtConfiguration.getDocumentation().split(",")).permitAll()
                // .antMatchers(HttpMethod.POST, jwtConfiguration.getUriGet().split(",")).permitAll()
                // .antMatchers(HttpMethod.POST, jwtConfiguration.getUriPost().split(",")).permitAll()
                /*
                 * Any other requests must be authenticated
                 */
               // .anyRequest().permitAll();
                .anyRequest().authenticated();
                
    }

}
