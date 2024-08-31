package mx.infotec.mascotams.service;

import mx.infotec.mascotams.service.dto.MascotaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.mascotams.domain.Mascota}.
 */
public interface MascotaService {
    /**
     * Save a mascota.
     *
     * @param mascotaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MascotaDTO> save(MascotaDTO mascotaDTO);

    /**
     * Updates a mascota.
     *
     * @param mascotaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MascotaDTO> update(MascotaDTO mascotaDTO);

    /**
     * Partially updates a mascota.
     *
     * @param mascotaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MascotaDTO> partialUpdate(MascotaDTO mascotaDTO);

    /**
     * Get all the mascotas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MascotaDTO> findAll(Pageable pageable);

    /**
     * Returns the number of mascotas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" mascota.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MascotaDTO> findOne(String id);

    /**
     * Delete the "id" mascota.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
