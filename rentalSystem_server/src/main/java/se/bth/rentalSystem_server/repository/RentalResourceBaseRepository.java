package se.bth.rentalSystem_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import se.bth.rentalSystem_server.models.Category;
import se.bth.rentalSystem_server.models.RentalResource;
import se.bth.rentalSystem_server.models.ResourceStatus;
import se.bth.rentalSystem_server.models.User;

import java.util.List;

@NoRepositoryBean
public interface RentalResourceBaseRepository<T extends RentalResource> extends JpaRepository<T, Long> {
    List<RentalResource> findByName(@Param("resource_name") String name);

    List<RentalResource> findByCategory(@Param("resource_category") Category category);

    List<RentalResource> findByOwner(@Param("resource_owner") User owner);

    List<RentalResource> findByStatus(@Param("resource_status") ResourceStatus status);
}
