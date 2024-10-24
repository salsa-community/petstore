package mx.infotec.repository;

import mx.infotec.domain.Tarea;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Tarea entity.
 */
@Repository
public interface TareaRepository extends MongoRepository<Tarea, String> {}
