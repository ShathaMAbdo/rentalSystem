package se.bth.rentalSystem_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.bth.rentalSystem_server.models.AvailableTime;

@Transactional
public interface AvailableTimeRepository extends JpaRepository<AvailableTime,Long> {
}
