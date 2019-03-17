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
import se.bth.rentalSystem_server.repository.SkillRepository;
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
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // All tests are conducted on a non-genuine database
    public class SkillTest {
        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        SkillRepository repository;
        @Autowired
        AvailableTimeRepository Trepo;
        @Autowired
        private UserRepository userRepo;

        private User getUser() {
            User user =User.builder().firstName("alex").lastName("Jonson").userName("alex").email("alex@bth.se")
                    .password("1234").passwordConfirm("1234").build();
            userRepo.save(user);
            return user;
        }
        @Test
        public void should_find_no_skills_if_repository_is_empty() {
            Iterable<Skill> skills= repository.findAll();

            assertThat(skills).isEmpty();
        }

        @Test
        public void should_store_a_skill() {
            List<AvailableTime> availableTimes = getAvailableTimes();
            User user=getUser();
            Skill skill = repository.save(Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("taxi driver").name("driver").owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).proof("123-856-999")
                    .status(ResourceStatus.AVAILABLE).build());

            Assertions.assertThat(skill.getAvailableTimes())
                    .hasSize(2)
                    .containsExactlyInAnyOrder(availableTimes.get(0),availableTimes.get(1) );
            assertThat(skill).hasFieldOrPropertyWithValue("category", Category.Personal);
            assertThat(skill).hasFieldOrPropertyWithValue("description", "taxi driver");
            assertThat(skill).hasFieldOrPropertyWithValue("name", "driver");
            assertThat(skill).hasFieldOrPropertyWithValue("owner",userRepo.findByUserName("alex").get());
            assertThat(skill).hasFieldOrPropertyWithValue("pricePerHour", 5.0);
            assertThat(skill).hasFieldOrPropertyWithValue("quantity", 1);
            assertThat(skill).hasFieldOrPropertyWithValue("proof", "123-856-999");
            assertThat(skill).hasFieldOrPropertyWithValue("status", ResourceStatus.AVAILABLE);

        }

        @Test
        public void should_delete_all_skill() {
            List<AvailableTime> availableTimes = getAvailableTimes();
            User user=getUser();
            entityManager.persist(Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("taxi driver").name("driver").owner(userRepo.findByUserName("alex").get()).pricePerHour(50.0).quantity(1).proof("123-856-999")
                    .status(ResourceStatus.AVAILABLE).build());
            entityManager.persist(Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("Moving furniture").name("porter").owner(userRepo.findByUserName("alex").get()).pricePerHour(500.0).quantity(5).proof("987-258-000")
                    .status(ResourceStatus.AVAILABLE).build());

            repository.deleteAll();

            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        public void should_find_all_skills() {
            List<AvailableTime> availableTimes = getAvailableTimes();
            User user=getUser();
            Skill skill1 = Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("taxi driver").name("driver").owner(userRepo.findByUserName("alex").get()).pricePerHour(50.0).quantity(1).proof("123-856-999")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill1);

            Skill skill2 =Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("Moving furniture").name("porter").owner(userRepo.findByUserName("alex").get()).pricePerHour(500.0).quantity(5).proof("987-258-000")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill2);

            Skill skill3 = Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("houses painter").name("painter").owner(userRepo.findByUserName("alex").get()).pricePerHour(500.0).quantity(5).proof("252-258-555")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill3);

            Iterable<Skill> skills = repository.findAll();

            assertThat(skills).hasSize(3).contains(skill1, skill2, skill3);
        }

        @Test
        public void should_find_skill_by_id() {
            List<AvailableTime> availableTimes = getAvailableTimes();
            User user=getUser();
            Skill skill1 = Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("taxi driver").name("driver").owner(userRepo.findByUserName("alex").get()).pricePerHour(50.0).quantity(1).proof("123-856-999")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill1);

            Skill skill2 =Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("Moving furniture").name("porter").owner(userRepo.findByUserName("alex").get()).pricePerHour(500.0).quantity(5).proof("987-258-000")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill2);

            Skill foundSkill = repository.findById(skill2.getId()).get();

            assertThat(foundSkill).isEqualTo(skill2);
        }

        @Test
        public void should_find_skill_by_proof() {
            List<AvailableTime> availableTimes = getAvailableTimes();
            User user=getUser();
            Skill skill1 = Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("taxi driver").name("driver").owner(userRepo.findByUserName("alex").get()).pricePerHour(50.0).quantity(1).proof("123-856-999")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill1);

            Skill skill2 =Skill.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Personal)
                    .description("Moving furniture").name("porter").owner(userRepo.findByUserName("alex").get()).pricePerHour(500.0).quantity(5).proof("987-258-000")
                    .status(ResourceStatus.AVAILABLE).build();
            entityManager.persist(skill2);

            Skill foundSkill = repository.findByProof(skill2.getProof()).get();

            assertThat(foundSkill).isEqualTo(skill2);
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
    }