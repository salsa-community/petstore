package mx.infotec.service.impl;

import java.util.Optional;
import mx.infotec.domain.Tarea;
import mx.infotec.repository.TareaRepository;
import mx.infotec.service.TareaService;
import mx.infotec.service.dto.TareaDTO;
import mx.infotec.service.mapper.TareaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link mx.infotec.domain.Tarea}.
 */
@Service
public class TareaServiceImpl implements TareaService {

    private static final Logger LOG = LoggerFactory.getLogger(TareaServiceImpl.class);

    private final TareaRepository tareaRepository;

    private final TareaMapper tareaMapper;

    public TareaServiceImpl(TareaRepository tareaRepository, TareaMapper tareaMapper) {
        this.tareaRepository = tareaRepository;
        this.tareaMapper = tareaMapper;
    }

    @Override
    public TareaDTO save(TareaDTO tareaDTO) {
        LOG.debug("Request to save Tarea : {}", tareaDTO);
        Tarea tarea = tareaMapper.toEntity(tareaDTO);
        tarea = tareaRepository.save(tarea);
        return tareaMapper.toDto(tarea);
    }

    @Override
    public TareaDTO update(TareaDTO tareaDTO) {
        LOG.debug("Request to update Tarea : {}", tareaDTO);
        Tarea tarea = tareaMapper.toEntity(tareaDTO);
        tarea = tareaRepository.save(tarea);
        return tareaMapper.toDto(tarea);
    }

    @Override
    public Optional<TareaDTO> partialUpdate(TareaDTO tareaDTO) {
        LOG.debug("Request to partially update Tarea : {}", tareaDTO);

        return tareaRepository
            .findById(tareaDTO.getId())
            .map(existingTarea -> {
                tareaMapper.partialUpdate(existingTarea, tareaDTO);

                return existingTarea;
            })
            .map(tareaRepository::save)
            .map(tareaMapper::toDto);
    }

    @Override
    public Page<TareaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tareas");
        return tareaRepository.findAll(pageable).map(tareaMapper::toDto);
    }

    @Override
    public Optional<TareaDTO> findOne(String id) {
        LOG.debug("Request to get Tarea : {}", id);
        return tareaRepository.findById(id).map(tareaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Tarea : {}", id);
        tareaRepository.deleteById(id);
    }
}
