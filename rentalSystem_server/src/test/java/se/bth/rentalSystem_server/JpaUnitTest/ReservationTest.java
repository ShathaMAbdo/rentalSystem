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
import se.bth.rentalSystem_server.repository.RentalResourceRepository;
import se.bth.rentalSystem_server.repository.ReservationRepository;
import se.bth.rentalSystem_server.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
public class ReservationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ReservationRepository repository;
    @Autowired
    private AvailableTimeRepository Trepo;
    @Autowired
    private RentalResourceRepository renRepo;
    @Autowired
    private UserRepository userRepo;


    @Test
    public void should_find_no_reservations_if_repository_is_empty() {
        Iterable<Reservation> reservations = repository.findAll();

        assertThat(reservations).isEmpty();
    }

    @Test
    public void should_store_a_reservation() {
        List<AvailableTime> rentalTimes = getRentalTimes();
        List<RentalResource> rentalResources = getRentalResources();
        Reservation reservation = repository.save(Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(1L).get()).renter(userRepo.findByUserName("alex").get())
                .build());
        Assertions.assertThat(reservation.getRentalTimes())
                .hasSize(2)
                .containsExactlyInAnyOrder(rentalTimes.get(0), rentalTimes.get(1));
        assertThat(reservation).hasFieldOrPropertyWithValue("status", ReservationStatus.RESERVED);
        assertThat(reservation).hasFieldOrPropertyWithValue("renter", userRepo.findByUserName("alex").get());
        assertThat(reservation).hasFieldOrPropertyWithValue("rentalResource", renRepo.findById(1L).get());

    }

    @Test
    public void should_find_all_reservations() {
        List<AvailableTime> rentalTimes = getRentalTimes();
        List<RentalResource> rentalResources = getRentalResources();

        Reservation reservation1 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(1L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation1);

        Reservation reservation2 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(2L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation2);

        Reservation reservation3 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(3L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation3);

        Iterable<Reservation> reservations = repository.findAll();

        assertThat(reservations).hasSize(3).contains(reservation1, reservation2, reservation3);
    }

    @Test
    public void should_find_reservation_by_id() {
        List<AvailableTime> rentalTimes = getRentalTimes();
        List<RentalResource> rentalResources = getRentalResources();

        Reservation reservation1 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(1L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation1);

        Reservation reservation2 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(2L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation2);
        Reservation foundReservation = repository.findById(reservation2.getId()).get();

        assertThat(foundReservation).isEqualTo(reservation2);
    }

    @Test
    public void should_find_reservation_by_RentalResource() {
        List<AvailableTime> rentalTimes = getRentalTimes();
        List<RentalResource> rentalResources = getRentalResources();

        Reservation reservation1 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(1L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation1);

        Reservation reservation2 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(2L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation2);
        Reservation foundReservation = repository.findByRentalResource(reservation2.getRentalResource()).get(0);
        assertThat(foundReservation).isEqualTo(reservation2);
    }

    @Test
    public void should_find_reservation_by_Renter() {
        List<AvailableTime> rentalTimes = getRentalTimes();
        List<RentalResource> rentalResources = getRentalResources();

        Reservation reservation1 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(1L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation1);

        Reservation reservation2 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(2L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation2);
        List<Reservation> foundReservations = repository.findByRenter(reservation2.getRenter());
        List<Reservation> realList = new ArrayList<>();
        realList.add(reservation1);
        realList.add(reservation2);
        assertThat(foundReservations).isEqualTo(realList);
    }

    @Test
    public void should_find_reservation_by_statuse() {
        List<AvailableTime> rentalTimes = getRentalTimes();
        List<RentalResource> rentalResources = getRentalResources();

        Reservation reservation1 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(1L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation1);

        Reservation reservation2 = Reservation.builder().reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                .status(ReservationStatus.RESERVED).rentalResource(renRepo.findById(2L).get()).renter(userRepo.findByUserName("alex").get())
                .build();
        entityManager.persist(reservation2);
        List<Reservation> foundReservations = repository.findByStatus(reservation2.getStatus());
        List<Reservation> realList = new ArrayList<>();
        realList.add(reservation1);
        realList.add(reservation2);
        assertThat(foundReservations).isEqualTo(realList);
    }

    private List<AvailableTime> getRentalTimes() {
        List<AvailableTime> rentalTimes = new ArrayList<>();
        AvailableTime rentalTime1 = AvailableTime.builder().date(LocalDate.of(2019, 3, 30)).start(LocalTime.of(13, 0, 0))
                .end(LocalTime.of(15, 0, 0)).build();
        rentalTimes.add(rentalTime1);
        AvailableTime rentalTime2 = AvailableTime.builder().date(LocalDate.of(2019, 3, 31)).start(LocalTime.of(13, 0, 0))
                .end(LocalTime.of(14, 0, 0)).build();
        rentalTimes.add(rentalTime2);
       // Trepo.saveAll(rentalTimes);
        return rentalTimes;
    }

    private User getUser() {
        User user = User.builder().firstName("alex").lastName("Jonson").userName("alex").email("alex@bth.se")
                .password("1234").passwordConfirm("1234").build();
        userRepo.save(user);
         user = User.builder().firstName("Erik").lastName("Jonson").userName("Erik").email("erik@bth.se")
                .password("1234").passwordConfirm("1234").build();
        userRepo.save(user);
        return user;
    }

    private List<RentalResource> getRentalResources() {
        List<RentalResource> rentalResources = new ArrayList<>();
        List<AvailableTime> availableTimes = new ArrayList<>();
        User user = getUser();
        AvailableTime availableTime = AvailableTime.builder().day(DayOfWeek.SATURDAY).start(LocalTime.of(12, 0, 0))
                .end(LocalTime.of(15, 0, 0)).build();
        availableTimes.add(availableTime);
        availableTime = AvailableTime.builder().day(DayOfWeek.SUNDAY).start(LocalTime.of(12, 0, 0))
                .end(LocalTime.of(15, 0, 0)).build();
        availableTimes.add(availableTime);
        Trepo.saveAll(availableTimes);

        RentalResource resource1 = Product.builder().registerDate(LocalDateTime.now()).category(Category.Car_Motorbike)
                .description("BMW 2019").name("car").availableTimes(availableTimes).owner(userRepo.findByUserName("alex").get()).pricePerHour(5.0).quantity(1).serialNumber("123-856-999")
                .status(ResourceStatus.AVAILABLE).build();
        renRepo.save(resource1);

        RentalResource resource2 = Product.builder().registerDate(LocalDateTime.now()).category(Category.Car_Motorbike)
                .description("Volvo 2014").name("car").availableTimes(availableTimes).owner(userRepo.findByUserName("alex").get()).pricePerHour(14.0).quantity(1).serialNumber("987-258-000")
                .status(ResourceStatus.AVAILABLE).build();
        renRepo.save(resource2);

        RentalResource resource3 = Product.builder().registerDate(LocalDateTime.now()).category(Category.Car_Motorbike)
                .description("Kia 2010").name("car").availableTimes(availableTimes).owner(userRepo.findByUserName("alex").get()).pricePerHour(10.0).quantity(1).serialNumber("222-258-654")
                .status(ResourceStatus.AVAILABLE).build();
        renRepo.save(resource3);
        rentalResources.add(resource1);
        rentalResources.add(resource2);
       // rentalResources.add(resource3);
        return rentalResources;
    }
}