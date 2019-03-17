package se.bth.rentalSystem_server.JpaUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import se.bth.rentalSystem_server.models.CustomerStatus;
import se.bth.rentalSystem_server.models.User;
import se.bth.rentalSystem_server.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // All tests are conducted on a non-genuine database
public class UserTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UserRepository repository;

    @Test
    public void should_find_no_customers_if_repository_is_empty() {
        Iterable<User> users = repository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    public void should_store_a_customer() {
        User user = repository.save(User.builder().firstName("Jack").lastName("semson").email("jack@bth.se")
        .city("Karlskrona").phon("07650586532").payCard("147-852-9631").status(CustomerStatus.ACTIVE).build());

        assertThat(user).hasFieldOrPropertyWithValue("firstName", "Jack");
        assertThat(user).hasFieldOrPropertyWithValue("lastName", "semson");
        assertThat(user).hasFieldOrPropertyWithValue("email", "jack@bth.se");
        assertThat(user).hasFieldOrPropertyWithValue("city", "Karlskrona");
        assertThat(user).hasFieldOrPropertyWithValue("payCard", "147-852-9631");
        assertThat(user).hasFieldOrPropertyWithValue("status", CustomerStatus.ACTIVE);
        assertThat(user).hasFieldOrPropertyWithValue("phon", "07650586532");
    }

    @Test
    public void should_delete_all_customer() {
        entityManager.persist(User.builder().firstName("Jack").lastName("semson").email("jack@bth.se")
                        .city("Karlskrona").phon("07650586532").payCard("147-852-9631").status(CustomerStatus.ACTIVE).build());
        entityManager.persist(User.builder().firstName("Adam").lastName("svenson").email("adam@bth.se")
                .city("Malmö").phon("0777142563").payCard("203-589-2222").status(CustomerStatus.ACTIVE).build());

        repository.deleteAll();

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void should_find_all_customers() {
        User user1 = User.builder().firstName("Jack").lastName("semson").email("jack@bth.se")
                .city("Karlskrona").phon("07650586532").payCard("147-852-9631").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user1);

        User user2 = User.builder().firstName("Adam").lastName("svenson").email("adam@bth.se")
                .city("Malmö").phon("0777142563").payCard("203-589-2222").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user2);

        User user3 = User.builder().firstName("Peter").lastName("Hungston").email("peter@bth.se")
                .city("Kalmar").phon("0765894321").payCard("147-855-2156").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user3);

        Iterable<User> customers = repository.findAll();

        assertThat(customers).hasSize(3).contains(user1, user2, user3);
    }

    @Test
    public void should_find_customer_by_id() {
        User user1 = User.builder().firstName("Jack").lastName("semson").email("jack@bth.se")
                .city("Karlskrona").phon("07650586532").payCard("147-852-9631").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user1);

        User user2 = User.builder().firstName("Adam").lastName("svenson").email("adam@bth.se")
                .city("Malmö").phon("0777142563").payCard("203-589-2222").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user2);

        User foundCustomer = repository.findById(user2.getId()).get();

        assertThat(foundCustomer).isEqualTo(user2);
    }

    @Test
    public void should_find_customer_by_email() {
        User user1 = User.builder().firstName("Jack").lastName("semson").email("jack@bth.se")
                .city("Karlskrona").phon("07650586532").payCard("147-852-9631").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user1);

        User user2 = User.builder().firstName("Adam").lastName("svenson").email("adam@bth.se")
                .city("Malmö").phon("0777142563").payCard("203-589-2222").status(CustomerStatus.ACTIVE).build();

        entityManager.persist(user2);

        Optional<User> foundCustomer = repository.findByEmail(user1.getEmail());

        assertThat(foundCustomer).isEqualTo(user1);
    }
}