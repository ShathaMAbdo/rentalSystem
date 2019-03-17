package se.bth.rentalSystem_server.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.bth.rentalSystem_server.models.Skill;

import java.util.Optional;

@Transactional
public interface SkillRepository extends RentalResourceBaseRepository<Skill> {
      Optional<Skill>  findByProof(@Param("proof") String proof);

}
