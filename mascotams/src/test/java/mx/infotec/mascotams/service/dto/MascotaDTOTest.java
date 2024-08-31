package mx.infotec.mascotams.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.mascotams.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MascotaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MascotaDTO.class);
        MascotaDTO mascotaDTO1 = new MascotaDTO();
        mascotaDTO1.setId("id1");
        MascotaDTO mascotaDTO2 = new MascotaDTO();
        assertThat(mascotaDTO1).isNotEqualTo(mascotaDTO2);
        mascotaDTO2.setId(mascotaDTO1.getId());
        assertThat(mascotaDTO1).isEqualTo(mascotaDTO2);
        mascotaDTO2.setId("id2");
        assertThat(mascotaDTO1).isNotEqualTo(mascotaDTO2);
        mascotaDTO1.setId(null);
        assertThat(mascotaDTO1).isNotEqualTo(mascotaDTO2);
    }
}
