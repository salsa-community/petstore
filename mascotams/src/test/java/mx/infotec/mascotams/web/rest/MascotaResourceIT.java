package mx.infotec.mascotams.web.rest;

import static mx.infotec.mascotams.domain.MascotaAsserts.*;
import static mx.infotec.mascotams.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.UUID;
import mx.infotec.mascotams.IntegrationTest;
import mx.infotec.mascotams.domain.Mascota;
import mx.infotec.mascotams.repository.MascotaRepository;
import mx.infotec.mascotams.service.dto.MascotaDTO;
import mx.infotec.mascotams.service.mapper.MascotaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link MascotaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MascotaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDAD = 200;
    private static final Integer UPDATED_EDAD = 199;

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/mascotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private MascotaMapper mascotaMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Mascota mascota;

    private Mascota insertedMascota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mascota createEntity() {
        return new Mascota()
            .nombre(DEFAULT_NOMBRE)
            .edad(DEFAULT_EDAD)
            .precio(DEFAULT_PRECIO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mascota createUpdatedEntity() {
        return new Mascota()
            .nombre(UPDATED_NOMBRE)
            .edad(UPDATED_EDAD)
            .precio(UPDATED_PRECIO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        mascota = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMascota != null) {
            mascotaRepository.delete(insertedMascota).block();
            insertedMascota = null;
        }
    }

    @Test
    void createMascota() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);
        var returnedMascotaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MascotaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Mascota in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMascota = mascotaMapper.toEntity(returnedMascotaDTO);
        assertMascotaUpdatableFieldsEquals(returnedMascota, getPersistedMascota(returnedMascota));

        insertedMascota = returnedMascota;
    }

    @Test
    void createMascotaWithExistingId() throws Exception {
        // Create the Mascota with an existing ID
        mascota.setId("existing_id");
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mascota.setNombre(null);

        // Create the Mascota, which fails.
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEdadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mascota.setEdad(null);

        // Create the Mascota, which fails.
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mascota.setPrecio(null);

        // Create the Mascota, which fails.
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMascotas() {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        // Get all the mascotaList
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
            .value(hasItem(mascota.getId()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].edad")
            .value(hasItem(DEFAULT_EDAD))
            .jsonPath("$.[*].precio")
            .value(hasItem(DEFAULT_PRECIO.doubleValue()))
            .jsonPath("$.[*].fechaNacimiento")
            .value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.[*].fotoContentType")
            .value(hasItem(DEFAULT_FOTO_CONTENT_TYPE))
            .jsonPath("$.[*].foto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    void getMascota() {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        // Get the mascota
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, mascota.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(mascota.getId()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.edad")
            .value(is(DEFAULT_EDAD))
            .jsonPath("$.precio")
            .value(is(DEFAULT_PRECIO.doubleValue()))
            .jsonPath("$.fechaNacimiento")
            .value(is(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.fotoContentType")
            .value(is(DEFAULT_FOTO_CONTENT_TYPE))
            .jsonPath("$.foto")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_FOTO)));
    }

    @Test
    void getNonExistingMascota() {
        // Get the mascota
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMascota() throws Exception {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mascota
        Mascota updatedMascota = mascotaRepository.findById(mascota.getId()).block();
        updatedMascota
            .nombre(UPDATED_NOMBRE)
            .edad(UPDATED_EDAD)
            .precio(UPDATED_PRECIO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);
        MascotaDTO mascotaDTO = mascotaMapper.toDto(updatedMascota);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mascotaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMascotaToMatchAllProperties(updatedMascota);
    }

    @Test
    void putNonExistingMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(UUID.randomUUID().toString());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mascotaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(UUID.randomUUID().toString());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(UUID.randomUUID().toString());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMascotaWithPatch() throws Exception {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mascota using partial update
        Mascota partialUpdatedMascota = new Mascota();
        partialUpdatedMascota.setId(mascota.getId());

        partialUpdatedMascota.nombre(UPDATED_NOMBRE).edad(UPDATED_EDAD).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMascota.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMascota))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Mascota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMascotaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMascota, mascota), getPersistedMascota(mascota));
    }

    @Test
    void fullUpdateMascotaWithPatch() throws Exception {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mascota using partial update
        Mascota partialUpdatedMascota = new Mascota();
        partialUpdatedMascota.setId(mascota.getId());

        partialUpdatedMascota
            .nombre(UPDATED_NOMBRE)
            .edad(UPDATED_EDAD)
            .precio(UPDATED_PRECIO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMascota.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMascota))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Mascota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMascotaUpdatableFieldsEquals(partialUpdatedMascota, getPersistedMascota(partialUpdatedMascota));
    }

    @Test
    void patchNonExistingMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(UUID.randomUUID().toString());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, mascotaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(UUID.randomUUID().toString());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(UUID.randomUUID().toString());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(mascotaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMascota() {
        // Initialize the database
        insertedMascota = mascotaRepository.save(mascota).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mascota
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, mascota.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mascotaRepository.count().block();
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

    protected Mascota getPersistedMascota(Mascota mascota) {
        return mascotaRepository.findById(mascota.getId()).block();
    }

    protected void assertPersistedMascotaToMatchAllProperties(Mascota expectedMascota) {
        assertMascotaAllPropertiesEquals(expectedMascota, getPersistedMascota(expectedMascota));
    }

    protected void assertPersistedMascotaToMatchUpdatableProperties(Mascota expectedMascota) {
        assertMascotaAllUpdatablePropertiesEquals(expectedMascota, getPersistedMascota(expectedMascota));
    }
}
