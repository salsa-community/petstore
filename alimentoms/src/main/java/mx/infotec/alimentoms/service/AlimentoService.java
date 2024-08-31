package mx.infotec.alimentoms.service;

import mx.infotec.alimentoms.service.dto.AlimentoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.alimentoms.domain.Alimento}.
 */
public interface AlimentoService {
    /**
     * Save a alimento.
     *
     * @param alimentoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AlimentoDTO> save(AlimentoDTO alimentoDTO);

    /**
     * Updates a alimento.
     *
     * @param alimentoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AlimentoDTO> update(AlimentoDTO alimentoDTO);

    /**
     * Partially updates a alimento.
     *
     * @param alimentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AlimentoDTO> partialUpdate(AlimentoDTO alimentoDTO);

    /**
     * Get all the alimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AlimentoDTO> findAll(Pageable pageable);

    /**
     * Returns the number of alimentos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" alimento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AlimentoDTO> findOne(String id);

    /**
     * Delete the "id" alimento.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
