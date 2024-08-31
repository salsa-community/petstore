package mx.infotec.alimentoms.domain;

import java.util.UUID;

public class AlimentoTestSamples {

    public static Alimento getAlimentoSample1() {
        return new Alimento().id("id1").nombre("nombre1");
    }

    public static Alimento getAlimentoSample2() {
        return new Alimento().id("id2").nombre("nombre2");
    }

    public static Alimento getAlimentoRandomSampleGenerator() {
        return new Alimento().id(UUID.randomUUID().toString()).nombre(UUID.randomUUID().toString());
    }
}
