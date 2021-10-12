package co.edu.unisimon.bienestar.business.domain.service;

import java.util.List;

import co.edu.unisimon.bienestar.business.domain.dto.external.PaisDto;

public interface IPaisExternalService {

    List<PaisDto> getCountriesByLangaugeSpeaking(String language);

}
