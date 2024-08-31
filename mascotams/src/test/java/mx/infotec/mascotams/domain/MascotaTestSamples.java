package mx.infotec.mascotams.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MascotaTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Mascota getMascotaSample1() {
        return new Mascota().id("id1").nombre("nombre1").edad(1);
    }

    public static Mascota getMascotaSample2() {
        return new Mascota().id("id2").nombre("nombre2").edad(2);
    }

    public static Mascota getMascotaRandomSampleGenerator() {
        return new Mascota().id(UUID.randomUUID().toString()).nombre(UUID.randomUUID().toString()).edad(intCount.incrementAndGet());
    }
}
