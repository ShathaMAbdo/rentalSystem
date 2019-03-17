package se.bth.rentalSystem_server.repository;

import org.springframework.transaction.annotation.Transactional;
import se.bth.rentalSystem_server.models.RentalResource;


@Transactional
public interface RentalResourceRepository extends RentalResourceBaseRepository<RentalResource> {


}
