package co.edu.unisimon.bienestar.business.http.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unisimon.bienestar.business.domain.constant.Language;
import co.edu.unisimon.bienestar.business.domain.constant.Message;
import co.edu.unisimon.bienestar.business.domain.dto.external.PaisDto;
import co.edu.unisimon.bienestar.business.domain.dto.util.ResponseDto;
import co.edu.unisimon.bienestar.business.domain.service.impl.PaisExternalService;
import co.edu.unisimon.bienestar.common.Logger;
import co.edu.unisimon.bienestar.common.MapUtil;
import co.edu.unisimon.bienestar.common.TokenUtil;
import co.edu.unisimon.bienestar.configuration.exception.BadRequestException;
import co.edu.unisimon.bienestar.configuration.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/paises")
@Api(value = "Servicios asociados a la API RestCountries")
public class PaisController extends Controller {

    static final String UNAVAILABLE_LANGUAGE = "No se encuentra disponible ese idioma.";

    private final PaisExternalService paisExternalService;

    public PaisController(PaisExternalService paisExternalService, Message message, TokenUtil tokenUtil) {
        super(message, tokenUtil);
        this.paisExternalService = paisExternalService;
    }

    @GetMapping
    @ApiOperation(value = "Listar los paises según su idioma oficial")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Listado obtenido exitosamente",
                    response = PaisDto.class, responseContainer = "List")
    })
    public ResponseDto<List<PaisDto>> getPaisesByLanguage(@ApiParam(value = "Idioma oficial", required = true)
                                                          @RequestParam(name = "idioma") String idioma,
                                                          HttpServletRequest request) {
        Logger.tracing(request);

        if (!Language.EN.name().equalsIgnoreCase(idioma) && !Language.ES.name().equalsIgnoreCase(idioma)) {
            throw new BadRequestException(UNAVAILABLE_LANGUAGE);

        }
        List<PaisDto> paises = paisExternalService.getCountriesByLangaugeSpeaking(idioma);
        if (paises.isEmpty()) {
            throw new ResourceNotFoundException(
                    message.getMessage(message.getCustomNoData(), MapUtil.getMap(Message.ENTITY, PaisDto.NAME))
            );        }
        return ResponseDto.create(paises);
    }

}
