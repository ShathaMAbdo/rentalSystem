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
import se.bth.rentalSystem_server.repository.ProductRepository;
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
public class ProductTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ProductRepository repository;
    @Autowired
    AvailableTimeRepository Trepo;
    @Autowired
    private UserRepository userRepo;

    @Test
    public void should_find_no_products_if_repository_is_empty() {
        Iterable<Product> products = repository.findAll();

        assertThat(products).isEmpty();
    }

    @Test
    public void should_store_a_product() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user=getUser();
        Product product = repository.save(Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("BMW 2019").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).serialNumber("123-856-999")
                .status(ResourceStatus.AVAILABLE).build());
        Assertions.assertThat(product.getAvailableTimes())
                .hasSize(2)
                .containsExactlyInAnyOrder(availableTimes.get(0),availableTimes.get(1) );

        assertThat(product).hasFieldOrPropertyWithValue("category", Category.Car_Motorbike);
        assertThat(product).hasFieldOrPropertyWithValue("description", "BMW 2019");
        assertThat(product).hasFieldOrPropertyWithValue("name", "car");
        assertThat(product).hasFieldOrPropertyWithValue("owner", userRepo.findByFirstName("alex").get(0));
        assertThat(product).hasFieldOrPropertyWithValue("pricePerHour", 5.0);
        assertThat(product).hasFieldOrPropertyWithValue("quantity", 1);
        assertThat(product).hasFieldOrPropertyWithValue("serialNumber", "123-856-999");
        assertThat(product).hasFieldOrPropertyWithValue("status", ResourceStatus.AVAILABLE);
    }

    @Test
    public void should_delete_all_product() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        entityManager.persist(Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("BMW 2019").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).serialNumber("123-856-999")
                .status(ResourceStatus.AVAILABLE).build());
        entityManager.persist(Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("Volvo 2014").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(14.0).quantity(1).serialNumber("987-258-000")
                .status(ResourceStatus.AVAILABLE).build());

        repository.deleteAll();

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void should_find_all_products() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        Product product1 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("BMW 2019").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).serialNumber("123-856-999")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product1);

        Product product2 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("Volvo 2014").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(14.0).quantity(1).serialNumber("987-258-000")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product2);

        Product product3 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("Kia 2010").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(10.0).quantity(1).serialNumber("222-258-654")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product3);

        Iterable<Product> products = repository.findAll();

        assertThat(products).hasSize(3).contains(product1, product2, product3);
    }

    @Test
    public void should_find_product_by_id() {
        User user = getUser();
        List<AvailableTime> availableTimes = getAvailableTimes();
        Product product1 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("BMW 2019").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).serialNumber("123-856-999")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product1);

        Product product2 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("Volvo 2014").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(14.0).quantity(1).serialNumber("987-258-000")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product2);

        Product foundProduct = repository.findById(product2.getId()).get();

        assertThat(foundProduct).isEqualTo(product2);
    }


    @Test
    public void should_find_product_by_SerialNumber() {
        List<AvailableTime> availableTimes = getAvailableTimes();
        User user = getUser();
        Product product1 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("BMW 2019").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).serialNumber("123-856-999")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product1);

        Product product2 = Product.builder().registerDate(LocalDateTime.now()).availableTimes(availableTimes).category(Category.Car_Motorbike)
                .description("Volvo 2014").name("car").owner(userRepo.findByUserName("alex").get()).pricePerHour(14.0).quantity(1).serialNumber("987-258-000")
                .status(ResourceStatus.AVAILABLE).build();
        entityManager.persist(product2);

        Product foundProduct = repository.findBySerialNumber(product2.getSerialNumber()).get();

        assertThat(foundProduct).isEqualTo(product2);
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
        User user =User.builder().firstName("alex").lastName("Jonson").userName("alex").email("alex@bth.se")
                .password("1234").passwordConfirm("1234").build();
        userRepo.save(user);
        return user;
    }
}