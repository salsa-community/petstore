package mx.infotec.alimentoms.web.rest;

import static mx.infotec.alimentoms.domain.AlimentoAsserts.*;
import static mx.infotec.alimentoms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.UUID;
import mx.infotec.alimentoms.IntegrationTest;
import mx.infotec.alimentoms.domain.Alimento;
import mx.infotec.alimentoms.repository.AlimentoRepository;
import mx.infotec.alimentoms.service.dto.AlimentoDTO;
import mx.infotec.alimentoms.service.mapper.AlimentoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AlimentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AlimentoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/alimentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @Autowired
    private AlimentoMapper alimentoMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Alimento alimento;

    private Alimento insertedAlimento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alimento createEntity() {
        return new Alimento()
            .nombre(DEFAULT_NOMBRE)
            .precio(DEFAULT_PRECIO)
            .descripcion(DEFAULT_DESCRIPCION)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alimento createUpdatedEntity() {
        return new Alimento()
            .nombre(UPDATED_NOMBRE)
            .precio(UPDATED_PRECIO)
            .descripcion(UPDATED_DESCRIPCION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        alimento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlimento != null) {
            alimentoRepository.delete(insertedAlimento).block();
            insertedAlimento = null;
        }
    }

    @Test
    void createAlimento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);
        var returnedAlimentoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AlimentoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Alimento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlimento = alimentoMapper.toEntity(returnedAlimentoDTO);
        assertAlimentoUpdatableFieldsEquals(returnedAlimento, getPersistedAlimento(returnedAlimento));

        insertedAlimento = returnedAlimento;
    }

    @Test
    void createAlimentoWithExistingId() throws Exception {
        // Create the Alimento with an existing ID
        alimento.setId("existing_id");
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alimento.setNombre(null);

        // Create the Alimento, which fails.
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alimento.setPrecio(null);

        // Create the Alimento, which fails.
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAlimentos() {
        // Initialize the database
        insertedAlimento = alimentoRepository.save(alimento).block();

        // Get all the alimentoList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(alimento.getId()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].precio")
            .value(hasItem(DEFAULT_PRECIO.doubleValue()))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION.toString()))
            .jsonPath("$.[*].fotoContentType")
            .value(hasItem(DEFAULT_FOTO_CONTENT_TYPE))
            .jsonPath("$.[*].foto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    void getAlimento() {
        // Initialize the database
        insertedAlimento = alimentoRepository.save(alimento).block();

        // Get the alimento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, alimento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(alimento.getId()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.precio")
            .value(is(DEFAULT_PRECIO.doubleValue()))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION.toString()))
            .jsonPath("$.fotoContentType")
            .value(is(DEFAULT_FOTO_CONTENT_TYPE))
            .jsonPath("$.foto")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    void getNonExistingAlimento() {
        // Get the alimento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAlimento() throws Exception {
        // Initialize the database
        insertedAlimento = alimentoRepository.save(alimento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alimento
        Alimento updatedAlimento = alimentoRepository.findById(alimento.getId()).block();
        updatedAlimento
            .nombre(UPDATED_NOMBRE)
            .precio(UPDATED_PRECIO)
            .descripcion(UPDATED_DESCRIPCION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(updatedAlimento);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, alimentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlimentoToMatchAllProperties(updatedAlimento);
    }

    @Test
    void putNonExistingAlimento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alimento.setId(UUID.randomUUID().toString());

        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, alimentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAlimento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alimento.setId(UUID.randomUUID().toString());

        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAlimento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alimento.setId(UUID.randomUUID().toString());

        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAlimentoWithPatch() throws Exception {
        // Initialize the database
        insertedAlimento = alimentoRepository.save(alimento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alimento using partial update
        Alimento partialUpdatedAlimento = new Alimento();
        partialUpdatedAlimento.setId(alimento.getId());

        partialUpdatedAlimento.nombre(UPDATED_NOMBRE).precio(UPDATED_PRECIO).foto(UPDATED_FOTO).fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAlimento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAlimento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alimento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlimentoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlimento, alimento), getPersistedAlimento(alimento));
    }

    @Test
    void fullUpdateAlimentoWithPatch() throws Exception {
        // Initialize the database
        insertedAlimento = alimentoRepository.save(alimento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alimento using partial update
        Alimento partialUpdatedAlimento = new Alimento();
        partialUpdatedAlimento.setId(alimento.getId());

        partialUpdatedAlimento
            .nombre(UPDATED_NOMBRE)
            .precio(UPDATED_PRECIO)
            .descripcion(UPDATED_DESCRIPCION)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAlimento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAlimento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alimento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlimentoUpdatableFieldsEquals(partialUpdatedAlimento, getPersistedAlimento(partialUpdatedAlimento));
    }

    @Test
    void patchNonExistingAlimento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alimento.setId(UUID.randomUUID().toString());

        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, alimentoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAlimento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alimento.setId(UUID.randomUUID().toString());

        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAlimento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alimento.setId(UUID.randomUUID().toString());

        // Create the Alimento
        AlimentoDTO alimentoDTO = alimentoMapper.toDto(alimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(alimentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Alimento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAlimento() {
        // Initialize the database
        insertedAlimento = alimentoRepository.save(alimento).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alimento
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, alimento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alimentoRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Alimento getPersistedAlimento(Alimento alimento) {
        return alimentoRepository.findById(alimento.getId()).block();
    }

    protected void assertPersistedAlimentoToMatchAllProperties(Alimento expectedAlimento) {
        assertAlimentoAllPropertiesEquals(expectedAlimento, getPersistedAlimento(expectedAlimento));
    }

    protected void assertPersistedAlimentoToMatchUpdatableProperties(Alimento expectedAlimento) {
        assertAlimentoAllUpdatablePropertiesEquals(expectedAlimento, getPersistedAlimento(expectedAlimento));
    }
}
