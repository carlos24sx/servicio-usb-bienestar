package co.edu.unisimon.bienestar.configuration.doc;


import com.fasterxml.classmate.TypeResolver;

import co.edu.unisimon.bienestar.business.domain.dto.util.ErrorDto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author William Torres
 * @version 1.0.0
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final Environment env;

    public SwaggerConfiguration(Environment env) {
        this.env = env;
    }

    /**
     * @return Contact object with developer information.
     */
    private Contact getContact() {
        return new Contact(
                env.getProperty("swagger.developer.name"),
                env.getProperty("swagger.developer.url"),
                env.getProperty("swagger.developer.email")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version(env.getProperty("swagger.api.info.version"))
                .title(env.getProperty("swagger.api.info.title"))
                .description(env.getProperty("swagger.api.info.description"))
                .contact(this.getContact())
                .build();
    }

    /**
     * We’ll define a SecurityConfiguration bean in our Swagger configuration – and set some defaults
     */
    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder().scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false).build();
    }

    private ApiKey apiKey() {
        return new ApiKey(
                env.getProperty("swagger.api.key.name"),
                env.getProperty("swagger.api.key.key-name"),
                env.getProperty("swagger.api.key.pass-as")
        );
    }

    /**
     * we need to define a security context.
     * <p>
     *
     * @return A class to represent a default set of authorizations to apply to each api operation To customize which request
     * mappings the list of authorizations are applied to Specify the custom includePatterns or requestMethods.
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope(
                env.getProperty("swagger.security.authorization.scope"),
                env.getProperty("swagger.security.authorization.description")
        );
        return Collections.singletonList(new SecurityReference(
                env.getProperty("swagger.api.key.name"),
                authorizationScopes)
        );
    }

    @Bean
    public Docket services() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        /*
         * HTTP responses of services are explicitly customized and specified
         */
        TypeResolver typeResolver = new TypeResolver();
        docket.useDefaultResponseMessages(false)
                .additionalModels(typeResolver.resolve(ErrorDto.class));
        ModelRef errorModel = new ModelRef("ErrorDto");
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(400)
                        .message("The server cannot or will not process the request due to something that is perceived as a client error.")
                        .responseModel(errorModel).build(),
                new ResponseMessageBuilder().code(401)
                        .message("The server understood the request but refuses to authorize it.")
                        .responseModel(errorModel)
                        .build(),
                new ResponseMessageBuilder().code(403)
                        .message("Accessing the resource you were trying to reach is forbidden.")
                        .responseModel(errorModel)
                        .build(),
                new ResponseMessageBuilder().code(404)
                        .message("The server can't find the requested resource.")
                        .responseModel(errorModel)
                        .build(),
                new ResponseMessageBuilder().code(405)
                        .message("The request method is known by the server but is not supported by the target resource.")
                        .responseModel(errorModel)
                        .build(),
                new ResponseMessageBuilder().code(500)
                        .message("The server encountered an unexpected condition that prevented it from fulfilling the request.")
                        .responseModel(errorModel)
                        .build());

        docket.globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);

        return docket
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .genericModelSubstitutes(ResponseEntity.class)
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }


}