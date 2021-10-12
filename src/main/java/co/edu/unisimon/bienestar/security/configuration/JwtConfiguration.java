package co.edu.unisimon.bienestar.security.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author William Torres
 * @version 1.0.0
 */

@Getter
@Component
@RefreshScope
public class JwtConfiguration {

    @Value("${security.jwt.header}")
    private String header;

    @Value("${security.jwt.prefix}")
    private String prefix;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.uri.get}")
    private String uriGet;

    @Value("${security.jwt.uri.post}")
    private String uriPost;

    @Value("${security.jwt.uri.put}")
    private String uriPut;

    @Value("${security.jwt.uri.delete}")
    private String uriDelete;

    @Value("${security.jwt.uri.patch}")
    private String uriPatch;

    @Value("${security.jwt.uri.documentation}")
    private String documentation;

}
