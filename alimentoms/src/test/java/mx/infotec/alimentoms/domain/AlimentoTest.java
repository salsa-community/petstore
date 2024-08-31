package mx.infotec.alimentoms.domain;

import static mx.infotec.alimentoms.domain.AlimentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.alimentoms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlimentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alimento.class);
        Alimento alimento1 = getAlimentoSample1();
        Alimento alimento2 = new Alimento();
        assertThat(alimento1).isNotEqualTo(alimento2);

        alimento2.setId(alimento1.getId());
        assertThat(alimento1).isEqualTo(alimento2);

        alimento2 = getAlimentoSample2();
        assertThat(alimento1).isNotEqualTo(alimento2);
    }
}
