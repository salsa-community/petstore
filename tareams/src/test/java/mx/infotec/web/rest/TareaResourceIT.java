package mx.infotec.web.rest;

import static mx.infotec.domain.TareaAsserts.*;
import static mx.infotec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import mx.infotec.IntegrationTest;
import mx.infotec.domain.Tarea;
import mx.infotec.repository.TareaRepository;
import mx.infotec.service.dto.TareaDTO;
import mx.infotec.service.mapper.TareaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link TareaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TareaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_LIMITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_LIMITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tareas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private TareaMapper tareaMapper;

    @Autowired
    private MockMvc restTareaMockMvc;

    private Tarea tarea;

    private Tarea insertedTarea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createEntity() {
        return new Tarea().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION).fechaLimite(DEFAULT_FECHA_LIMITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createUpdatedEntity() {
        return new Tarea().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);
    }

    @BeforeEach
    public void initTest() {
        tarea = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTarea != null) {
            tareaRepository.delete(insertedTarea);
            insertedTarea = null;
        }
    }

    @Test
    void createTarea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);
        var returnedTareaDTO = om.readValue(
            restTareaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tareaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TareaDTO.class
        );

        // Validate the Tarea in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTarea = tareaMapper.toEntity(returnedTareaDTO);
        assertTareaUpdatableFieldsEquals(returnedTarea, getPersistedTarea(returnedTarea));

        insertedTarea = returnedTarea;
    }

    @Test
    void createTareaWithExistingId() throws Exception {
        // Create the Tarea with an existing ID
        tarea.setId("existing_id");
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTareaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tareaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTareas() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea);

        // Get all the tareaList
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarea.getId())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaLimite").value(hasItem(DEFAULT_FECHA_LIMITE.toString())));
    }

    @Test
    void getTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea);

        // Get the tarea
        restTareaMockMvc
            .perform(get(ENTITY_API_URL_ID, tarea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarea.getId()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaLimite").value(DEFAULT_FECHA_LIMITE.toString()));
    }

    @Test
    void getNonExistingTarea() throws Exception {
        // Get the tarea
        restTareaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea
        Tarea updatedTarea = tareaRepository.findById(tarea.getId()).orElseThrow();
        updatedTarea.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);
        TareaDTO tareaDTO = tareaMapper.toDto(updatedTarea);

        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tareaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tareaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTareaToMatchAllProperties(updatedTarea);
    }

    @Test
    void putNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tareaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tareaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tareaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tareaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTarea, tarea), getPersistedTarea(tarea));
    }

    @Test
    void fullUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).fechaLimite(UPDATED_FECHA_LIMITE);

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTareaUpdatableFieldsEquals(partialUpdatedTarea, getPersistedTarea(partialUpdatedTarea));
    }

    @Test
    void patchNonExistingTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tareaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tareaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tareaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTarea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarea.setId(UUID.randomUUID().toString());

        // Create the Tarea
        TareaDTO tareaDTO = tareaMapper.toDto(tarea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tareaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTarea() throws Exception {
        // Initialize the database
        insertedTarea = tareaRepository.save(tarea);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarea
        restTareaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tareaRepository.count();
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

    protected Tarea getPersistedTarea(Tarea tarea) {
        return tareaRepository.findById(tarea.getId()).orElseThrow();
    }

    protected void assertPersistedTareaToMatchAllProperties(Tarea expectedTarea) {
        assertTareaAllPropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }

    protected void assertPersistedTareaToMatchUpdatableProperties(Tarea expectedTarea) {
        assertTareaAllUpdatablePropertiesEquals(expectedTarea, getPersistedTarea(expectedTarea));
    }
}
