package mx.infotec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TareaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TareaDTO.class);
        TareaDTO tareaDTO1 = new TareaDTO();
        tareaDTO1.setId("id1");
        TareaDTO tareaDTO2 = new TareaDTO();
        assertThat(tareaDTO1).isNotEqualTo(tareaDTO2);
        tareaDTO2.setId(tareaDTO1.getId());
        assertThat(tareaDTO1).isEqualTo(tareaDTO2);
        tareaDTO2.setId("id2");
        assertThat(tareaDTO1).isNotEqualTo(tareaDTO2);
        tareaDTO1.setId(null);
        assertThat(tareaDTO1).isNotEqualTo(tareaDTO2);
    }
}
