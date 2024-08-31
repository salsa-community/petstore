package mx.infotec.alimentoms.service.impl;

import mx.infotec.alimentoms.repository.AlimentoRepository;
import mx.infotec.alimentoms.service.AlimentoService;
import mx.infotec.alimentoms.service.dto.AlimentoDTO;
import mx.infotec.alimentoms.service.mapper.AlimentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.alimentoms.domain.Alimento}.
 */
@Service
public class AlimentoServiceImpl implements AlimentoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlimentoServiceImpl.class);

    private final AlimentoRepository alimentoRepository;

    private final AlimentoMapper alimentoMapper;

    public AlimentoServiceImpl(AlimentoRepository alimentoRepository, AlimentoMapper alimentoMapper) {
        this.alimentoRepository = alimentoRepository;
        this.alimentoMapper = alimentoMapper;
    }

    @Override
    public Mono<AlimentoDTO> save(AlimentoDTO alimentoDTO) {
        LOG.debug("Request to save Alimento : {}", alimentoDTO);
        return alimentoRepository.save(alimentoMapper.toEntity(alimentoDTO)).map(alimentoMapper::toDto);
    }

    @Override
    public Mono<AlimentoDTO> update(AlimentoDTO alimentoDTO) {
        LOG.debug("Request to update Alimento : {}", alimentoDTO);
        return alimentoRepository.save(alimentoMapper.toEntity(alimentoDTO)).map(alimentoMapper::toDto);
    }

    @Override
    public Mono<AlimentoDTO> partialUpdate(AlimentoDTO alimentoDTO) {
        LOG.debug("Request to partially update Alimento : {}", alimentoDTO);

        return alimentoRepository
            .findById(alimentoDTO.getId())
            .map(existingAlimento -> {
                alimentoMapper.partialUpdate(existingAlimento, alimentoDTO);

                return existingAlimento;
            })
            .flatMap(alimentoRepository::save)
            .map(alimentoMapper::toDto);
    }

    @Override
    public Flux<AlimentoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Alimentos");
        return alimentoRepository.findAllBy(pageable).map(alimentoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return alimentoRepository.count();
    }

    @Override
    public Mono<AlimentoDTO> findOne(String id) {
        LOG.debug("Request to get Alimento : {}", id);
        return alimentoRepository.findById(id).map(alimentoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete Alimento : {}", id);
        return alimentoRepository.deleteById(id);
    }
}
