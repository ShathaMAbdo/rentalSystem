package se.bth.rentalSystem_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.bth.rentalSystem_server.models.Role;
import se.bth.rentalSystem_server.models.RoleName;

import java.util.Optional;

@Transactional
public interface RoleRepository extends JpaRepository<Role,Long> {
      Optional<Role> findByName(RoleName name);
}
