package mx.infotec.mascotams.domain;

import static mx.infotec.mascotams.domain.MascotaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.mascotams.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MascotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mascota.class);
        Mascota mascota1 = getMascotaSample1();
        Mascota mascota2 = new Mascota();
        assertThat(mascota1).isNotEqualTo(mascota2);

        mascota2.setId(mascota1.getId());
        assertThat(mascota1).isEqualTo(mascota2);

        mascota2 = getMascotaSample2();
        assertThat(mascota1).isNotEqualTo(mascota2);
    }
}
