package se.bth.rentalSystem_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.bth.rentalSystem_server.Exceptions.NotPayedException;
import se.bth.rentalSystem_server.models.Reservation;
import se.bth.rentalSystem_server.repository.ReservationRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReservationController {
    private final Logger log = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    private ReservationRepository reservationRepo;

    public ReservationController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @GetMapping("/reservations")
    Collection<Reservation> reservations() {
        return reservationRepo.findAll();
    }

    @GetMapping("/reservations/{id}")
    ResponseEntity<?> getReservation(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepo.findById(id);
        return reservation.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/reservations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler({NotPayedException.class})
    ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) throws URISyntaxException {
        log.info("Request to create reservations: {}", reservation);
        try {
            reservation.payReservation();
        } catch (NotPayedException e) {
            System.out.println(e.getMessage());
        }
        Reservation result = reservationRepo.save(reservation);
        return ResponseEntity.created(new URI("/api/reservations/" + result.getId())).body(result);
    }

    @PutMapping("/reservations")
    ResponseEntity<Reservation> updateReservation(@Valid @RequestBody Reservation reservation) {
        log.info("Request to update reservations: {}", reservation);
        Reservation result = reservationRepo.save(reservation);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        log.info("Request to delete reservations: {}", id);
        Reservation result=reservationRepo.findById(id).get();
        if(result!=null) result.deletReservation();
        reservationRepo.save(result);
        return ResponseEntity.ok().body(result);
    }

}
