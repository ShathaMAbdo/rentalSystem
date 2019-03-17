package se.bth.rentalSystem_server.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class RentalResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double pricePerHour;
    private LocalDateTime registerDate;
    private ResourceStatus status = ResourceStatus.AVAILABLE;
    private Integer quantity = 1;
    private String description;
    private Category category;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AvailableTime> availableTimes;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    public void changeStatuse(String statuse) {
        this.status = ResourceStatus.valueOf(statuse);
    }

    public void resource_returned(boolean ok) {
       if (ok)
           this.status=ResourceStatus.AVAILABLE;
       else
           this.status=ResourceStatus.NOT_READY;
    }
}