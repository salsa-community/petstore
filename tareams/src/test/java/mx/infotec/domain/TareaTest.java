package mx.infotec.domain;

import static mx.infotec.domain.TareaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TareaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarea.class);
        Tarea tarea1 = getTareaSample1();
        Tarea tarea2 = new Tarea();
        assertThat(tarea1).isNotEqualTo(tarea2);

        tarea2.setId(tarea1.getId());
        assertThat(tarea1).isEqualTo(tarea2);

        tarea2 = getTareaSample2();
        assertThat(tarea1).isNotEqualTo(tarea2);
    }
}
