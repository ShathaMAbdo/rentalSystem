package se.bth.rentalSystem_server.JpaUnitTest;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import se.bth.rentalSystem_server.models.*;
import se.bth.rentalSystem_server.repository.AvailableTimeRepository;
import se.bth.rentalSystem_server.repository.KnowledgeRepository;
import se.bth.rentalSystem_server.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// All tests are conducted on a non-genuine database
public class knowledgeTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    KnowledgeRepository repository;
    @Autowired
    AvailableTimeRepository Trepo;
    @Autowired
    private UserRepository userRepo;

    @Test
    public void should_find_no_knowledges_if_repository_is_empty() {
        Iterable<Knowledge> knowledges = repository.findAll();

        assertThat(knowledges).isEmpty();
    }

    @Test
    public void should_store_a_knowledge() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();

        Knowledge knowledge = repository.save(Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of the automatic drill").name("information of drill").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(5.0).quantity(1).type(Type.CD).status(ResourceStatus.AVAILABLE).relatedProduct("drill").build());

        Assertions.assertThat(knowledge.getAvailableTimes())
                .hasSize(2)
                .containsExactlyInAnyOrder(availableTimes.get(0), availableTimes.get(1));
        assertThat(knowledge).hasFieldOrPropertyWithValue("category", Category.Others);
        assertThat(knowledge).hasFieldOrPropertyWithValue("description", "Information on the operation of the automatic drill");
        assertThat(knowledge).hasFieldOrPropertyWithValue("name", "information of drill");
        assertThat(knowledge).hasFieldOrPropertyWithValue("owner", userRepo.findByUserName("alex").get());
        assertThat(knowledge).hasFieldOrPropertyWithValue("pricePerHour", 5.0);
        assertThat(knowledge).hasFieldOrPropertyWithValue("quantity", 1);
        assertThat(knowledge).hasFieldOrPropertyWithValue("type", Type.CD);
        assertThat(knowledge).hasFieldOrPropertyWithValue("status", ResourceStatus.AVAILABLE);
        assertThat(knowledge).hasFieldOrPropertyWithValue("relatedProduct", "drill");

    }

    @Test
    public void should_delete_all_knowledge() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        entityManager.persist(Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of the automatic drill").name("information about drill").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(5.0).quantity(1).type(Type.CD).status(ResourceStatus.AVAILABLE).relatedProduct("drill").build());
        entityManager.persist(Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of grass cutting machine").name("information about grass cutting machine").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(5.0).quantity(1).type(Type.KATALOK).status(ResourceStatus.AVAILABLE).relatedProduct("grass cutting machine").build());

        repository.deleteAll();

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void should_find_all_knowledges() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        Knowledge knowledge1 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of the automatic drill").name("information about drill").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(5.0).quantity(1).type(Type.CD).status(ResourceStatus.AVAILABLE).relatedProduct("drill").build();
        entityManager.persist(knowledge1);

        Knowledge knowledge2 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of grass cutting machine").name("information about grass cutting machine").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(3.0).quantity(1).type(Type.KATALOK).status(ResourceStatus.AVAILABLE).relatedProduct("grass cutting machine").build();
        entityManager.persist(knowledge2);

        Knowledge knowledge3 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of dishwasher").name("Information about dishwasher").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(4.0).quantity(1).type(Type.KATALOK).status(ResourceStatus.AVAILABLE).relatedProduct("dishwasher").build();
        entityManager.persist(knowledge3);

        Iterable<Knowledge> knowledges = repository.findAll();

        assertThat(knowledges).hasSize(3).contains(knowledge1, knowledge2, knowledge3);
    }

    @Test
    public void should_find_knowledge_by_id() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        Knowledge knowledge1 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of the automatic drill").name("information about drill").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(5.0).quantity(1).type(Type.CD).status(ResourceStatus.AVAILABLE).relatedProduct("drill").build();
        entityManager.persist(knowledge1);

        Knowledge knowledge2 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of grass cutting machine").name("information about grass cutting machine").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(3.0).quantity(1).type(Type.KATALOK).status(ResourceStatus.AVAILABLE).relatedProduct("grass cutting machine").build();
        entityManager.persist(knowledge2);

        Knowledge foundKnowledge = repository.findById(knowledge2.getId()).get();

        assertThat(foundKnowledge).isEqualTo(knowledge2);
    }

    @Test
    public void should_find_Knowledge_by_relatedProduct() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        Knowledge knowledge1 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of the automatic drill").name("information about drill").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(5.0).quantity(1).type(Type.CD).status(ResourceStatus.AVAILABLE).relatedProduct("drill").build();
        entityManager.persist(knowledge1);

        Knowledge knowledge2 = Knowledge.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Others)
                .description("Information on the operation of grass cutting machine").name("information about grass cutting machine").owner(userRepo.findByUserName("alex").get())
                .pricePerHour(3.0).quantity(1).type(Type.KATALOK).status(ResourceStatus.AVAILABLE).relatedProduct("grass cutting machine").build();
        entityManager.persist(knowledge2);

        Knowledge foundKnowledge = repository.findByRelatedProduct(knowledge2.getRelatedProduct()).get();

        assertThat(foundKnowledge).isEqualTo(knowledge2);
    }

    private List<AvailableTime> getAvailableTimes() {
        List<AvailableTime> availableTimes = new ArrayList<>();
        AvailableTime availableTime = AvailableTime.builder().day(DayOfWeek.SATURDAY).start(LocalTime.of(12, 0, 0))
                .end(LocalTime.of(15, 0, 0)).build();
        availableTimes.add(availableTime);
        availableTime = AvailableTime.builder().day(DayOfWeek.SUNDAY).start(LocalTime.of(12, 0, 0))
                .end(LocalTime.of(15, 0, 0)).build();
        availableTimes.add(availableTime);
        Trepo.saveAll(availableTimes);
        return availableTimes;
    }

    private User getUser() {
        User user = User.builder().firstName("alex").lastName("Jonson").userName("alex").email("alex@bth.se")
                .password("1234").passwordConfirm("1234").build();
        userRepo.save(user);
        return user;
    }
}