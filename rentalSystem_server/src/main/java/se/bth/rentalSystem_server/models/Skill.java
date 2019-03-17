package se.bth.rentalSystem_server.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Skill extends RentalResource {
    private String proof;

    @Builder
    public Skill(Long id, String name, Double pricePerHour, LocalDateTime registerDate, ResourceStatus status, Integer quantity,
                 String description, Category category, List<AvailableTime> availableTimes, User owner, String proof) {
        super(id, name, pricePerHour, registerDate, status, quantity, description, category, availableTimes, owner);
        this.proof = proof;
    }
}

