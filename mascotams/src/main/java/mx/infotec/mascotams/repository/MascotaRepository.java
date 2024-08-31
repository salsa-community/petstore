package mx.infotec.mascotams.repository;

import mx.infotec.mascotams.domain.Mascota;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Mascota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MascotaRepository extends ReactiveMongoRepository<Mascota, String> {
    Flux<Mascota> findAllBy(Pageable pageable);
}
