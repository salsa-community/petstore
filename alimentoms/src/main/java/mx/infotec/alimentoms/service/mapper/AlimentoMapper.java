package mx.infotec.alimentoms.service.mapper;

import mx.infotec.alimentoms.domain.Alimento;
import mx.infotec.alimentoms.service.dto.AlimentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alimento} and its DTO {@link AlimentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlimentoMapper extends EntityMapper<AlimentoDTO, Alimento> {}
