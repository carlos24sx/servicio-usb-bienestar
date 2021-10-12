package co.edu.unisimon.bienestar.security.filters;


import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import co.edu.unisimon.bienestar.security.configuration.JwtConfiguration;
import co.edu.unisimon.bienestar.security.services.JwtTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author William Torres
 * @version 1.0.0
 * @apiNote Interfaces:
 * - BasicAuthenticationFilter: This filter is responsible for processing any request that has a HTTP request header of
 * Authorization with an authentication scheme of Basic and a Base64-encoded username:password token.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtConfiguration jwtConfiguration;

    private final JwtTokenService jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration, JwtTokenService jwtTokenProvider) {
        super(authenticationManager);
        this.jwtConfiguration = jwtConfiguration;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 1. Get the authentication header. Tokens are supposed to be passed in the authentication header
        String tokenHeader = request.getHeader(jwtConfiguration.getHeader());
        // 2. Validate the header and check the prefix
        if (tokenHeader == null || !tokenHeader.startsWith(jwtConfiguration.getPrefix())) {
            chain.doFilter(request, response); // If not valid, go to the next filter.
            return;
        }
        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.
        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.
        try {    // exceptions might be thrown in creating the claims if for example the token is expired
            // 3. Get the token and Validate the token
            Claims claims = jwtTokenProvider.getClaims(tokenHeader);
            String username = claims.getSubject();
            if (username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");
                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
            e.printStackTrace();
        }
        // Go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}
