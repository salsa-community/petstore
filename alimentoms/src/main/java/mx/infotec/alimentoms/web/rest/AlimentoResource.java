package mx.infotec.alimentoms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.alimentoms.repository.AlimentoRepository;
import mx.infotec.alimentoms.service.AlimentoService;
import mx.infotec.alimentoms.service.dto.AlimentoDTO;
import mx.infotec.alimentoms.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link mx.infotec.alimentoms.domain.Alimento}.
 */
@RestController
@RequestMapping("/api/alimentos")
public class AlimentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlimentoResource.class);

    private static final String ENTITY_NAME = "alimentomsAlimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlimentoService alimentoService;

    private final AlimentoRepository alimentoRepository;

    public AlimentoResource(AlimentoService alimentoService, AlimentoRepository alimentoRepository) {
        this.alimentoService = alimentoService;
        this.alimentoRepository = alimentoRepository;
    }

    /**
     * {@code POST  /alimentos} : Create a new alimento.
     *
     * @param alimentoDTO the alimentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alimentoDTO, or with status {@code 400 (Bad Request)} if the alimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AlimentoDTO>> createAlimento(@Valid @RequestBody AlimentoDTO alimentoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Alimento : {}", alimentoDTO);
        if (alimentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new alimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return alimentoService
            .save(alimentoDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/alimentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /alimentos/:id} : Updates an existing alimento.
     *
     * @param id the id of the alimentoDTO to save.
     * @param alimentoDTO the alimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alimentoDTO,
     * or with status {@code 400 (Bad Request)} if the alimentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AlimentoDTO>> updateAlimento(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AlimentoDTO alimentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Alimento : {}, {}", id, alimentoDTO);
        if (alimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alimentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return alimentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return alimentoService
                    .update(alimentoDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /alimentos/:id} : Partial updates given fields of an existing alimento, field will ignore if it is null
     *
     * @param id the id of the alimentoDTO to save.
     * @param alimentoDTO the alimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alimentoDTO,
     * or with status {@code 400 (Bad Request)} if the alimentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alimentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AlimentoDTO>> partialUpdateAlimento(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AlimentoDTO alimentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Alimento partially : {}, {}", id, alimentoDTO);
        if (alimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alimentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return alimentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AlimentoDTO> result = alimentoService.partialUpdate(alimentoDTO);

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
     * {@code GET  /alimentos} : get all the alimentos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alimentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AlimentoDTO>>> getAllAlimentos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Alimentos");
        return alimentoService
            .countAll()
            .zipWith(alimentoService.findAll(pageable).collectList())
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
     * {@code GET  /alimentos/:id} : get the "id" alimento.
     *
     * @param id the id of the alimentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alimentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AlimentoDTO>> getAlimento(@PathVariable("id") String id) {
        LOG.debug("REST request to get Alimento : {}", id);
        Mono<AlimentoDTO> alimentoDTO = alimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alimentoDTO);
    }

    /**
     * {@code DELETE  /alimentos/:id} : delete the "id" alimento.
     *
     * @param id the id of the alimentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAlimento(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Alimento : {}", id);
        return alimentoService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
