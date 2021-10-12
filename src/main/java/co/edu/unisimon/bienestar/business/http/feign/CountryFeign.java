package co.edu.unisimon.bienestar.business.http.feign;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.unisimon.bienestar.business.domain.dto.external.PaisDto;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "RestCountries", url = "https://restcountries.eu/rest/v2/", fallbackFactory = CountryFallbackFactory.class)
public interface CountryFeign {

    @GetMapping(value = "/lang/{language}")
    List<PaisDto> getCountriesByLangaugeSpeaking(@RequestParam(name = "language") String language);

}

@Slf4j
@Component
class CountryFallbackFactory implements FallbackFactory<CountryFeign> {

    @Override
    public CountryFeign create(Throwable throwable) {
        return loginDto -> {
            log.error(throwable.getMessage());
            return new ArrayList<>();
        };
    }
}
