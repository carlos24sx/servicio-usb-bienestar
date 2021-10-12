package co.edu.unisimon.bienestar.business.http.controller;

import io.swagger.annotations.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import co.edu.unisimon.bienestar.business.domain.constant.Message;
import co.edu.unisimon.bienestar.business.domain.dto.PersonaDto;
import co.edu.unisimon.bienestar.business.domain.dto.custom.list.PersonaListDto;
import co.edu.unisimon.bienestar.business.domain.dto.util.ResponseDto;
import co.edu.unisimon.bienestar.business.domain.mapper.PersonaMapper;
import co.edu.unisimon.bienestar.business.domain.persistence.entity.PersonaEntity;
import co.edu.unisimon.bienestar.business.domain.service.impl.PersonaService;
import co.edu.unisimon.bienestar.common.Logger;
import co.edu.unisimon.bienestar.common.MapUtil;
import co.edu.unisimon.bienestar.common.TokenUtil;
import co.edu.unisimon.bienestar.configuration.exception.BadRequestException;
import co.edu.unisimon.bienestar.configuration.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
@Api(value = "Servicios asociados a la entiedad Persona")
public class PersonaController extends Controller {

    private final PersonaMapper personaMapper;
    private final PersonaService personaService;


    public PersonaController(PersonaService personaService, Message message, TokenUtil tokenUtil) {
        super(message, tokenUtil);
        this.personaService = personaService;
        this.personaMapper = Mappers.getMapper(PersonaMapper.class);
    }

    @GetMapping
    @ApiOperation(value = "Listar todas las personas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Listado obtenido exitosamente",
                    response = PersonaDto.class, responseContainer = "List")
    })
    public ResponseDto<List<PersonaListDto>> getPersonas(HttpServletRequest request) {
        Logger.tracing(request);
        List<PersonaEntity> personaEntities = personaService.findAll();
        if (personaEntities.isEmpty()) {
            throw new ResourceNotFoundException(
                    message.getMessage(message.getCustomNoData(), MapUtil.getMap(Message.ENTITY, PersonaDto.NAME))
            );
        }
        List<PersonaListDto> personaDtos = this.personaMapper.convertEntityListToCustomDtoList(personaEntities);
        return ResponseDto.create(personaDtos);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener una persona por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Objeto obtenido exitosamente", response = PersonaDto.class)
    })
    public ResponseDto<PersonaDto> getPersona(@ApiParam(value = "Id del objeto Persona", required = true)
                                              @PathVariable(name = "id") Long id,
                                              HttpServletRequest request) {
        Logger.tracing(request);
        Optional<PersonaEntity> optionalPersona = personaService.findById(id);
        if (optionalPersona.isEmpty()) {
            throw new ResourceNotFoundException(
                    message.getMessage(message.getCustomNoData(), MapUtil.getMap(Message.ENTITY, PersonaDto.NAME))
            );
        }
        PersonaDto personaDto = this.personaMapper.convertEntityToDto(optionalPersona.get());
        return ResponseDto.create(personaDto);
    }

    @PostMapping
    @ApiOperation(value = "Guardar una persona")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Objeto guardado exitosamente", response = PersonaDto.class)
    })
    public ResponseDto<PersonaDto> createPersona(@ApiParam(value = "Datos de la persona", required = true)
                                                 @RequestBody PersonaDto personaDto, HttpServletRequest request) {
        Logger.tracing(request);

        if (Objects.nonNull(personaDto.getId())) {
            throw new BadRequestException(
                    message.getMessage(message.getNullField(), MapUtil.getMap(Message.FIELD, "ID"))
            );
        }
        PersonaEntity persona = personaMapper.convertDtoToEntity(personaDto);
        persona = personaService.save(persona);
        personaDto = this.personaMapper.convertEntityToDto(persona);
        return ResponseDto.create(personaDto);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar una persona")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Objeto actualizado exitosamente", response = PersonaDto.class)
    })
    public ResponseDto<PersonaDto> updatePersona(@ApiParam(value = "Id del objeto Persona", required = true)
                                                 @PathVariable(name = "id") Long id,
                                                 @ApiParam(value = "Datos de la persona", required = true)
                                                 @RequestBody PersonaDto personaDto, HttpServletRequest request) {
        Logger.tracing(request);

        // Set ID
        personaDto.setId(id);

        // Validate existence
        Optional<PersonaEntity> optionalPersona = personaService.findById(id);
        if (optionalPersona.isEmpty()) {
            throw new ResourceNotFoundException(
                    message.getMessage(message.getCustomNoData(), MapUtil.getMap(Message.ENTITY, PersonaDto.NAME))
            );
        }

        PersonaEntity persona = personaMapper.convertDtoToEntity(personaDto);
        persona = personaService.save(persona);
        personaDto = this.personaMapper.convertEntityToDto(persona);
        return ResponseDto.create(personaDto);
    }

}
