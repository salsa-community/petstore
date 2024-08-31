package mx.infotec.mascotams.service.mapper;

import mx.infotec.mascotams.domain.Mascota;
import mx.infotec.mascotams.service.dto.MascotaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mascota} and its DTO {@link MascotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MascotaMapper extends EntityMapper<MascotaDTO, Mascota> {}
