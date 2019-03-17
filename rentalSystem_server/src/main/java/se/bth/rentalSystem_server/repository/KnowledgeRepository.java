package se.bth.rentalSystem_server.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.bth.rentalSystem_server.models.Knowledge;
import se.bth.rentalSystem_server.models.Type;

import java.util.List;
import java.util.Optional;

@Transactional
public interface KnowledgeRepository extends RentalResourceBaseRepository<Knowledge> {

    Optional<Knowledge> findByRelatedProduct(@Param("relatedProduct") String relatedProduct);

    List<Knowledge> findByType(@Param("type") Type type);
}
