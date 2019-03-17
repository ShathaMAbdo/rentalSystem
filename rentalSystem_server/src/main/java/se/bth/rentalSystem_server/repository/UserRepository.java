package se.bth.rentalSystem_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import se.bth.rentalSystem_server.models.CustomerStatus;
import se.bth.rentalSystem_server.models.User;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByFirstName(@Param("first_name") String firstName);

    List<User> findByLastName(@Param("last_name") String lastName);

    Optional<User> findByEmail(@Param("email") String email);

    List<User> findByPhon(@Param("phone") String phon);

    List<User> findByCity(@Param("city") String city);

    List<User> findByStatus(@Param("Customer_status") CustomerStatus status);

    Optional<User> findByPayCard(@Param("payCard") String payCard);

    //@Query("SELECT p FROM Person p where p.firstName = ?1 AND p.lastName = ?2")
    List<User> findByFirstNameAndLastName(@Param("first_name") String fName, @Param("last_name") String lName);

    Optional<User> findByUserNameOrEmail(String userName, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);
}
