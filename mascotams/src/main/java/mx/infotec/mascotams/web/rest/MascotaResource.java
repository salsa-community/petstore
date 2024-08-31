package mx.infotec.mascotams.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.mascotams.repository.MascotaRepository;
import mx.infotec.mascotams.service.MascotaService;
import mx.infotec.mascotams.service.dto.MascotaDTO;
import mx.infotec.mascotams.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link mx.infotec.mascotams.domain.Mascota}.
 */
@RestController
@RequestMapping("/api/mascotas")
public class MascotaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MascotaResource.class);

    private static final String ENTITY_NAME = "mascotamsMascota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MascotaService mascotaService;

    private final MascotaRepository mascotaRepository;

    public MascotaResource(MascotaService mascotaService, MascotaRepository mascotaRepository) {
        this.mascotaService = mascotaService;
        this.mascotaRepository = mascotaRepository;
    }

    /**
     * {@code POST  /mascotas} : Create a new mascota.
     *
     * @param mascotaDTO the mascotaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mascotaDTO, or with status {@code 400 (Bad Request)} if the mascota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MascotaDTO>> createMascota(@Valid @RequestBody MascotaDTO mascotaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Mascota : {}", mascotaDTO);
        if (mascotaDTO.getId() != null) {
            throw new BadRequestAlertException("A new mascota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return mascotaService
            .save(mascotaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/mascotas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /mascotas/:id} : Updates an existing mascota.
     *
     * @param id the id of the mascotaDTO to save.
     * @param mascotaDTO the mascotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mascotaDTO,
     * or with status {@code 400 (Bad Request)} if the mascotaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mascotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MascotaDTO>> updateMascota(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody MascotaDTO mascotaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mascota : {}, {}", id, mascotaDTO);
        if (mascotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mascotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mascotaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return mascotaService
                    .update(mascotaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /mascotas/:id} : Partial updates given fields of an existing mascota, field will ignore if it is null
     *
     * @param id the id of the mascotaDTO to save.
     * @param mascotaDTO the mascotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mascotaDTO,
     * or with status {@code 400 (Bad Request)} if the mascotaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mascotaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mascotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MascotaDTO>> partialUpdateMascota(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody MascotaDTO mascotaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mascota partially : {}, {}", id, mascotaDTO);
        if (mascotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mascotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mascotaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MascotaDTO> result = mascotaService.partialUpdate(mascotaDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /mascotas} : get all the mascotas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mascotas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MascotaDTO>>> getAllMascotas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Mascotas");
        return mascotaService
            .countAll()
            .zipWith(mascotaService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /mascotas/:id} : get the "id" mascota.
     *
     * @param id the id of the mascotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mascotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MascotaDTO>> getMascota(@PathVariable("id") String id) {
        LOG.debug("REST request to get Mascota : {}", id);
        Mono<MascotaDTO> mascotaDTO = mascotaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mascotaDTO);
    }

    /**
     * {@code DELETE  /mascotas/:id} : delete the "id" mascota.
     *
     * @param id the id of the mascotaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMascota(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Mascota : {}", id);
        return mascotaService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
