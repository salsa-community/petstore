package mx.infotec.alimentoms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.alimentoms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlimentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlimentoDTO.class);
        AlimentoDTO alimentoDTO1 = new AlimentoDTO();
        alimentoDTO1.setId("id1");
        AlimentoDTO alimentoDTO2 = new AlimentoDTO();
        assertThat(alimentoDTO1).isNotEqualTo(alimentoDTO2);
        alimentoDTO2.setId(alimentoDTO1.getId());
        assertThat(alimentoDTO1).isEqualTo(alimentoDTO2);
        alimentoDTO2.setId("id2");
        assertThat(alimentoDTO1).isNotEqualTo(alimentoDTO2);
        alimentoDTO1.setId(null);
        assertThat(alimentoDTO1).isNotEqualTo(alimentoDTO2);
    }
}
