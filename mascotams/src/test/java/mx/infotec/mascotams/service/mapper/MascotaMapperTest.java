package mx.infotec.mascotams.service.mapper;

import static mx.infotec.mascotams.domain.MascotaAsserts.*;
import static mx.infotec.mascotams.domain.MascotaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MascotaMapperTest {

    private MascotaMapper mascotaMapper;

    @BeforeEach
    void setUp() {
        mascotaMapper = new MascotaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMascotaSample1();
        var actual = mascotaMapper.toEntity(mascotaMapper.toDto(expected));
        assertMascotaAllPropertiesEquals(expected, actual);
    }
}
