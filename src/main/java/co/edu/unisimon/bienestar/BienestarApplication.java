package co.edu.unisimon.bienestar;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import co.edu.unisimon.bienestar.configuration.ApplicationConfiguration;

import java.util.Locale;

@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class BienestarApplication {

    public static void main(String[] args) {
        ApplicationConfiguration.run(args);
    }

    /**
     * Set the object represents a specific geographical, political, or cultural region.
     *
     * @see java.util.Locale
     * @see org.springframework.web.servlet.i18n.SessionLocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es"));
        return slr;
    }

}
