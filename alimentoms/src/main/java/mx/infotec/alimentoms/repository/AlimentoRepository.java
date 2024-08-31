package mx.infotec.alimentoms.repository;

import mx.infotec.alimentoms.domain.Alimento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Alimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlimentoRepository extends ReactiveMongoRepository<Alimento, String> {
    Flux<Alimento> findAllBy(Pageable pageable);
}
