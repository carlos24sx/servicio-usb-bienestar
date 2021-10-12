package co.edu.unisimon.bienestar.configuration.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * When the natural behavior of DispatcherServlet was modified in relation to the handling of error 404,
     * it caused the automatic configuration of static resource handlers to be lost; for this reason this method
     * of the WebMvcConfigurer interface is overwritten.
     *
     * @see co.edu.unisimon.bienestar.configuration.ApplicationConfiguration#run(String[] args)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Add a resource handler for serving static resources based on the specified URL path patterns.
        registry.addResourceHandler("swagger-ui.html")
                // Add one or more resource locations from which to serve static content.
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
