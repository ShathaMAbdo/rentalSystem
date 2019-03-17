package se.bth.rentalSystem_server.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Product extends RentalResource {
    private String serialNumber;
    @Lob
    private Byte[] image;

    @Builder

    public Product(Long id, String name, Double pricePerHour, LocalDateTime registerDate, ResourceStatus status, Integer quantity,
                   String description, Category category, List<AvailableTime> availableTimes, User owner, String serialNumber, Byte[] image) {
        super(id, name, pricePerHour, registerDate, status, quantity, description, category, availableTimes, owner);
        this.serialNumber = serialNumber;
        this.image = image;
    }
}
