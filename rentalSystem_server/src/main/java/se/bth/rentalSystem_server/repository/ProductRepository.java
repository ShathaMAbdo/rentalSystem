package se.bth.rentalSystem_server.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.bth.rentalSystem_server.models.Product;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ProductRepository extends RentalResourceBaseRepository<Product> {
    Optional<Product> findBySerialNumber(@Param("serialNumber") String serialNumber);
}
