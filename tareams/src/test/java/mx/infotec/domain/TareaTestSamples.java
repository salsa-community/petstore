package mx.infotec.domain;

import java.util.UUID;

public class TareaTestSamples {

    public static Tarea getTareaSample1() {
        return new Tarea().id("id1").nombre("nombre1").descripcion("descripcion1");
    }

    public static Tarea getTareaSample2() {
        return new Tarea().id("id2").nombre("nombre2").descripcion("descripcion2");
    }

    public static Tarea getTareaRandomSampleGenerator() {
        return new Tarea().id(UUID.randomUUID().toString()).nombre(UUID.randomUUID().toString()).descripcion(UUID.randomUUID().toString());
    }
}
