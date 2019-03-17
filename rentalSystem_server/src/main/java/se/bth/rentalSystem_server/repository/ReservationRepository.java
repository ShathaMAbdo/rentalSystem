package se.bth.rentalSystem_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import se.bth.rentalSystem_server.models.RentalResource;
import se.bth.rentalSystem_server.models.Reservation;
import se.bth.rentalSystem_server.models.ReservationStatus;
import se.bth.rentalSystem_server.models.User;

import java.util.List;

@Transactional
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByRentalResource(RentalResource rentalResource);

    List<Reservation> findByRenter(User renter);

    List<Reservation> findByStatus(ReservationStatus status);

}
