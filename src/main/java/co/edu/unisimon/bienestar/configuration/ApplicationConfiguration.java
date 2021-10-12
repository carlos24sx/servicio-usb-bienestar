package co.edu.unisimon.bienestar.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import co.edu.unisimon.bienestar.BienestarApplication;
import co.edu.unisimon.bienestar.configuration.web.WebMvcConfiguration;

/**
 * ApplicationConfiguration set the main application settings
 *
 * @author William Torres
 * @version 1.0
 * @see SocketUtils
 * @see StringUtils
 * @since April, 2020
 */
@Slf4j
public class ApplicationConfiguration {

    private ApplicationConfiguration() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Determines an available port within the specified range and sets it to {@code server.port} property.
     *
     * @throws IllegalStateException If an available port is not found, the exception will be logged and Spring Boot
     *                               will try to start on port 8080 (default port).
     */
    static void setRandomPort() {
        try {
            /*
             * With the help of the {@code SocketUtils} class, an available port is determined.
             */
            String portDefined = System.getProperty("server.port");
            if (StringUtils.isEmpty(portDefined)) {
                /*
                 * To define the minimum and maximum values, consult the staff in charge
                 * of the architecture or infrastructure of the project.
                 */
                /**
                 * Minimum value of the range of ports from which the application will select an available one.
                 */
                int minPort = 50000;
                /**
                 * Maximum value of the range of ports from which the application will select an available one.
                 */
                int maxPort = 50100;
                Integer port = SocketUtils.findAvailableTcpPort(minPort, maxPort);
                System.setProperty("server.port", String.valueOf(port));
            }
        } catch (IllegalStateException e) {
            log.error("The application can't running. Error: ".concat(e.toString()));
        }
    }

    /**
     * Call the main method. The DispatchServlet is customized to
     * throw an exception instead of sending response 404.
     *
     * @param args The arguments with which the application starts
     * @see WebMvcConfiguration#addResourceHandlers(ResourceHandlerRegistry)
     */
    public static void run(String[] args) {
        ApplicationConfiguration.setRandomPort();
        ApplicationContext applicationContext = SpringApplication.run(BienestarApplication.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) applicationContext.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }

}
