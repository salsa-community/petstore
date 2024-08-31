package mx.infotec.mascotams.service.impl;

import mx.infotec.mascotams.repository.MascotaRepository;
import mx.infotec.mascotams.service.MascotaService;
import mx.infotec.mascotams.service.dto.MascotaDTO;
import mx.infotec.mascotams.service.mapper.MascotaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.mascotams.domain.Mascota}.
 */
@Service
public class MascotaServiceImpl implements MascotaService {

    private static final Logger LOG = LoggerFactory.getLogger(MascotaServiceImpl.class);

    private final MascotaRepository mascotaRepository;

    private final MascotaMapper mascotaMapper;

    public MascotaServiceImpl(MascotaRepository mascotaRepository, MascotaMapper mascotaMapper) {
        this.mascotaRepository = mascotaRepository;
        this.mascotaMapper = mascotaMapper;
    }

    @Override
    public Mono<MascotaDTO> save(MascotaDTO mascotaDTO) {
        LOG.debug("Request to save Mascota : {}", mascotaDTO);
        return mascotaRepository.save(mascotaMapper.toEntity(mascotaDTO)).map(mascotaMapper::toDto);
    }

    @Override
    public Mono<MascotaDTO> update(MascotaDTO mascotaDTO) {
        LOG.debug("Request to update Mascota : {}", mascotaDTO);
        return mascotaRepository.save(mascotaMapper.toEntity(mascotaDTO)).map(mascotaMapper::toDto);
    }

    @Override
    public Mono<MascotaDTO> partialUpdate(MascotaDTO mascotaDTO) {
        LOG.debug("Request to partially update Mascota : {}", mascotaDTO);

        return mascotaRepository
            .findById(mascotaDTO.getId())
            .map(existingMascota -> {
                mascotaMapper.partialUpdate(existingMascota, mascotaDTO);

                return existingMascota;
            })
            .flatMap(mascotaRepository::save)
            .map(mascotaMapper::toDto);
    }

    @Override
    public Flux<MascotaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Mascotas");
        return mascotaRepository.findAllBy(pageable).map(mascotaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return mascotaRepository.count();
    }

    @Override
    public Mono<MascotaDTO> findOne(String id) {
        LOG.debug("Request to get Mascota : {}", id);
        return mascotaRepository.findById(id).map(mascotaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Mascota : {}", id);
        return mascotaRepository.deleteById(id);
    }
}
