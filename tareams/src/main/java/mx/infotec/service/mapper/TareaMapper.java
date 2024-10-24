package mx.infotec.service.mapper;

import mx.infotec.domain.Tarea;
import mx.infotec.service.dto.TareaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tarea} and its DTO {@link TareaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TareaMapper extends EntityMapper<TareaDTO, Tarea> {}
