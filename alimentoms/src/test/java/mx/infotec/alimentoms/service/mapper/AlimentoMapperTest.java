package mx.infotec.alimentoms.service.mapper;

import static mx.infotec.alimentoms.domain.AlimentoAsserts.*;
import static mx.infotec.alimentoms.domain.AlimentoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlimentoMapperTest {

    private AlimentoMapper alimentoMapper;

    @BeforeEach
    void setUp() {
        alimentoMapper = new AlimentoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlimentoSample1();
        var actual = alimentoMapper.toEntity(alimentoMapper.toDto(expected));
        assertAlimentoAllPropertiesEquals(expected, actual);
    }
}
