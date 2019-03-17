package se.bth.rentalSystem_server.models;

import lombok.*;
import se.bth.rentalSystem_server.Exceptions.DeneyCancelException;
import se.bth.rentalSystem_server.Exceptions.NotPayedException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime reservationTime;
    private ReservationStatus status;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AvailableTime> rentalTimes;

    @ManyToOne(fetch = FetchType.LAZY)
    private RentalResource rentalResource;

    @ManyToOne(fetch = FetchType.LAZY)
    private User renter;

    public void changeStatus(String newStatus) {
        this.status = ReservationStatus.valueOf(newStatus);
    }

    public Double totalPrice() {
        Double totalPrice = 0.0;
        for (AvailableTime rt : this.getRentalTimes()) {
            long noOfHour = ChronoUnit.HOURS.between(rt.getStart(), rt.getEnd());
            totalPrice = totalPrice + noOfHour * this.rentalResource.getPricePerHour();
        }
        return totalPrice;
    }

    public void payReservation() throws NotPayedException {
        Boolean isPayed = transfer(this.renter, this.rentalResource.getOwner(), this.totalPrice());
        if (!isPayed)
            if (LocalDate.now().isBefore(rentalTimes.stream().map(u -> u.getDate()).min(LocalDate::compareTo).get())) {
                this.status = ReservationStatus.UNCOMPLETED;
                throw new NotPayedException("Payment not received, you can pay or cancel before rental time start.");
            } else {
                status = ReservationStatus.CANCELED_BY_ADMIN;
                this.rentalResource.setStatus(ResourceStatus.AVAILABLE);
                throw new NotPayedException("Payment not received,your reservation was canceled by Admin");
            }
    }

    private Boolean transfer(User renter, User owner, Double totalPrice) {
        return true;// called swish or sweedbank
    }

    public void deletReservation() {
        if ((LocalDate.now()).isBefore(rentalTimes.stream().map(u -> u.getDate()).min(LocalDate::compareTo).get())) {
            this.status = ReservationStatus.CANCELED_BY_CLIENT;
            this.rentalResource.setStatus(ResourceStatus.AVAILABLE);
        } else
            try {
                throw new DeneyCancelException();
            } catch (DeneyCancelException e) {
                System.out.println(e.getMessage());
            }
    }

}
