package se.bth.rentalSystem_server.handling;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import se.bth.rentalSystem_server.Exceptions.EmptyRentalTime;
import se.bth.rentalSystem_server.Exceptions.TimeNotAvailableException;
import se.bth.rentalSystem_server.Exceptions.TimeNotValidateException;
import se.bth.rentalSystem_server.models.*;
import se.bth.rentalSystem_server.repository.*;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository userrepo;
    private final ProductRepository productrepo;
    private final AvailableTimeRepository avaiTimeRepo;
    private final KnowledgeRepository knowledgeRepo;
    private final SkillRepository skillRepo;
    private final ReservationRepository reservationRepo;
    private final RentalResourceRepository rentalResourceRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public Initializer(UserRepository repository, ProductRepository productrepo, AvailableTimeRepository avaiTimeRepo, KnowledgeRepository knowledgeRepository, SkillRepository skillRepo, ReservationRepository reservationRepo, RentalResourceRepository rentalResourceRepo, UserRepository userRepo, RoleRepository roleRepo) {
        this.userrepo = repository;
        this.productrepo = productrepo;
        this.avaiTimeRepo = avaiTimeRepo;
        this.knowledgeRepo = knowledgeRepository;
        this.skillRepo = skillRepo;
        this.reservationRepo = reservationRepo;
        this.rentalResourceRepo = rentalResourceRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... strings) {
        addPersons();
        addProduct();
        addKnowledge();
        addSkill();
        addReservations();
    }

    private void addReservations() {
        List<AvailableTime> rentalTimes = new ArrayList<>();
        RentalResource rentalResource = rentalResourceRepo.findById(2L).get();
        User renter = userrepo.findByUserName("Fredrik").get();
        addRentalTimeToList(rentalTimes, rentalResource, LocalDate.of(2019, 3, 3),
                LocalTime.of(8, 0, 0), LocalTime.of(16, 0, 0));
        if (!rentalTimes.isEmpty()) {
            rentalResource.changeStatuse("RESERVED");
            rentalResourceRepo.save(rentalResource);
            Reservation reservation = Reservation.builder().status(ReservationStatus.RESERVED).reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                    .rentalResource(rentalResource).renter(renter).build();
            reservationRepo.save(reservation);
        } else try {
            throw new EmptyRentalTime();
        } catch (EmptyRentalTime e) {
            System.out.println(e.getMessage());
        }
        rentalTimes.clear();
        renter =  userrepo.findByUserName("Erik").get();
        addRentalTimeToList(rentalTimes, rentalResource, LocalDate.of(2019, 3, 3),
                LocalTime.of(10, 0, 0), LocalTime.of(16, 0, 0));
        if (!rentalTimes.isEmpty()) {
            rentalResource.changeStatuse("RESERVED");
            rentalResourceRepo.save(rentalResource);
            Reservation reservation = Reservation.builder().status(ReservationStatus.RESERVED).reservationTime(LocalDateTime.now()).rentalTimes(rentalTimes)
                    .rentalResource(rentalResource).renter(renter).build();
            reservationRepo.save(reservation);
        } else try {
            throw new EmptyRentalTime();
        } catch (EmptyRentalTime e) {
            System.out.println(e.getMessage());
        }
        reservationRepo.findAll().forEach(System.out::println);
    }

    private void addRentalTimeToList(List<AvailableTime> rentalTimes, RentalResource borrowedStuff, LocalDate d, LocalTime start, LocalTime end) {
        AvailableTime rentalTime = AvailableTime.builder().date(d).day(d.getDayOfWeek()).start(start).end(end).build();
        LocalDate lastRentaldate = lastRentalDate(borrowedStuff);
        if (!isAvailable(borrowedStuff.getAvailableTimes(), rentalTime))
            try {
                throw new TimeNotAvailableException();
            } catch (TimeNotAvailableException e) {
                System.out.println(e.getMessage());
            }
        else if (!isValidate(lastRentaldate, rentalTime))
            try {
                throw new TimeNotValidateException();
            } catch (TimeNotValidateException e) {
                System.out.println(e.getMessage());
            }
        else {
            avaiTimeRepo.save(rentalTime);
            rentalTimes.add(rentalTime);
        }
    }

    private LocalDate lastRentalDate(RentalResource borrowedStuff) {
        List<Reservation> resList = reservationRepo.findByRentalResource(borrowedStuff);
        LocalDate maxDate = LocalDate.of(1900, 1, 1);
        for (Reservation a : resList) {
            LocalDate localDate = a.getRentalTimes().stream().map(u -> u.getDate()).max(LocalDate::compareTo).get();
            if (localDate.isAfter(maxDate)) maxDate = localDate;
        }
        return maxDate;
    }

    private void addSkill() {
        List<AvailableTime> availableTimes = new ArrayList<>();
        AvailableTime e = AvailableTime.builder().day(DayOfWeek.MONDAY).start(LocalTime.of(8, 0))
                .end(LocalTime.of(15, 0)).build();
        availableTimes.add(e);
        avaiTimeRepo.save(e);
        e = AvailableTime.builder().day(DayOfWeek.TUESDAY).start(LocalTime.of(10, 0))
                .end(LocalTime.of(17, 30)).build();
        availableTimes.add(e);
        e = AvailableTime.builder().day(DayOfWeek.FRIDAY).start(LocalTime.of(12, 0))
                .end(LocalTime.of(20, 0)).build();
        availableTimes.add(e);
        avaiTimeRepo.saveAll(availableTimes);
        User user =  userrepo.findByUserName("Fredrik").get();
        Skill s = Skill.builder().name("driving licence").registerDate(LocalDateTime.now()).quantity(1).pricePerHour(300.0).status(ResourceStatus.AVAILABLE).category(Category.Personal).description("Driving trucks")
                .proof("12365-985-987").availableTimes(availableTimes).owner(user).build();
        skillRepo.save(s);
        availableTimes = new ArrayList<>();
        e = AvailableTime.builder().day(DayOfWeek.SATURDAY).start(LocalTime.of(0, 0))
                .end(LocalTime.of(23, 59)).build();
        availableTimes.add(e);
        avaiTimeRepo.save(e);
        e = AvailableTime.builder().day(DayOfWeek.SUNDAY).start(LocalTime.of(0, 0))
                .end(LocalTime.of(23, 59)).build();
        availableTimes.add(e);
        avaiTimeRepo.saveAll(availableTimes);
        user =  userrepo.findByUserName("Anna").get();
        s = Skill.builder().name("Moving furniture").registerDate(LocalDateTime.now()).quantity(1).pricePerHour(500.5).status(ResourceStatus.AVAILABLE).category(Category.Books).description("Experience in packing and transporting home furniture")
                .proof("333-298-745").availableTimes(availableTimes).owner(user).build();
        skillRepo.save(s);

        skillRepo.findAll().forEach(System.out::println);
    }

    private void addKnowledge() {
        List<AvailableTime> availableTimes = new ArrayList<>();
        AvailableTime e = AvailableTime.builder().day(DayOfWeek.MONDAY).start(LocalTime.of(8, 0))
                .end(LocalTime.of(15, 0)).build();
        availableTimes.add(e);
        avaiTimeRepo.save(e);
        e = AvailableTime.builder().day(DayOfWeek.TUESDAY).start(LocalTime.of(10, 0))
                .end(LocalTime.of(17, 30)).build();
        availableTimes.add(e);
        e = AvailableTime.builder().day(DayOfWeek.FRIDAY).start(LocalTime.of(12, 0))
                .end(LocalTime.of(20, 0)).build();
        availableTimes.add(e);
        avaiTimeRepo.saveAll(availableTimes);
        User user = userrepo.findById(3L).get();
        Knowledge n = Knowledge.builder().name("Katalok of Camera").registerDate(LocalDateTime.now()).quantity(1).pricePerHour(25.0).status(ResourceStatus.AVAILABLE).category(Category.Books).description("book description how camera works")
                .type(Type.KATALOK).availableTimes(availableTimes).relatedProduct("Sony Camera").owner(user).build();
        knowledgeRepo.save(n);
        availableTimes = new ArrayList<>();
        e = AvailableTime.builder().day(DayOfWeek.SATURDAY).start(LocalTime.of(0, 0))
                .end(LocalTime.of(23, 59)).build();
        availableTimes.add(e);
        avaiTimeRepo.save(e);
        e = AvailableTime.builder().day(DayOfWeek.SUNDAY).start(LocalTime.of(0, 0))
                .end(LocalTime.of(23, 59)).build();
        availableTimes.add(e);
        avaiTimeRepo.saveAll(availableTimes);
        user = userrepo.findById(4L).get();
        n = Knowledge.builder().name("Printer Drive").quantity(1).registerDate(LocalDateTime.now()).pricePerHour(20.5).status(ResourceStatus.AVAILABLE).category(Category.Books).description("Drive for install identifier for printer")
                .type(Type.CD).availableTimes(availableTimes).owner(user).relatedProduct("HP printer").build();
        knowledgeRepo.save(n);

        knowledgeRepo.findAll().forEach(System.out::println);
    }

    private void addProduct() {
        List<AvailableTime> availableTimes = new ArrayList<>();
        AvailableTime e = AvailableTime.builder().day(DayOfWeek.MONDAY).start(LocalTime.of(5, 0))
                .end(LocalTime.of(10, 0)).build();
        availableTimes.add(e);
        avaiTimeRepo.save(e);
        e = AvailableTime.builder().day(DayOfWeek.TUESDAY).start(LocalTime.of(12, 0))
                .end(LocalTime.of(17, 30)).build();
        availableTimes.add(e);
        e = AvailableTime.builder().day(DayOfWeek.FRIDAY).start(LocalTime.of(12, 0))
                .end(LocalTime.of(17, 30)).build();
        availableTimes.add(e);
        avaiTimeRepo.saveAll(availableTimes);
        User user = userrepo.findById(1L).get();
        Product p = Product.builder().name("Camera").quantity(1).pricePerHour(50.0).registerDate(LocalDateTime.now()).status(ResourceStatus.AVAILABLE).category(Category.Electronics_Computers).description("Home camera")
                .serialNumber("11-25k-oi12").availableTimes(availableTimes).owner(user).build();
        productrepo.save(p);
        availableTimes = new ArrayList<>();
        e = AvailableTime.builder().day(DayOfWeek.SATURDAY).start(LocalTime.of(0, 0))
                .end(LocalTime.of(23, 59)).build();
        availableTimes.add(e);
        avaiTimeRepo.save(e);
        e = AvailableTime.builder().day(DayOfWeek.SUNDAY).start(LocalTime.of(0, 0))
                .end(LocalTime.of(23, 59)).build();
        availableTimes.add(e);
        avaiTimeRepo.saveAll(availableTimes);
        user = userrepo.findById(2L).get();
        p = Product.builder().name("Car").quantity(1).registerDate(LocalDateTime.now()).pricePerHour(500.0).status(ResourceStatus.AVAILABLE).category(Category.Electronics_Computers).description("Volvo")
                .serialNumber("1235-25lj-vc12").availableTimes(availableTimes).owner(user).build();
        productrepo.save(p);

        productrepo.findAll().forEach(System.out::println);
    }

    private void addPersons() {
        Set<Role> roles=new HashSet<>();
        Role role1=Role.builder().name(RoleName.ROLE_USER).build();
        roleRepo.save(role1);
        roles.add(role1);
        User c = User.builder().firstName("Erik").lastName("Juhanson").email("erik@bth.se").phon("0760762135").city("Karlskrona").status(CustomerStatus
                .ACTIVE).payCard("111-528-9999").userName("Erik").password("1111").passwordConfirm("1111").roles(roles).build();
        userrepo.save(c);
        c = User.builder().firstName("Fredrik").lastName("Svensson").email("fridrek@bth.se").phon("0770772135").city("Karlshamen")
                .status(CustomerStatus.ACTIVE).payCard("124-318-5555").userName("Fredrik").password("2222").passwordConfirm("2222").roles(roles).build();
        userrepo.save(c);
        c = User.builder().firstName("Olle").lastName("Hamen").email("olle@bth.se").phon("0760762111").city("Malmö")
                .status(CustomerStatus.ACTIVE).payCard("214-333-9254").userName("Olle").password("3333").passwordConfirm("3333").roles(roles).build();
        userrepo.save(c);
        c = User.builder().firstName("Anna").lastName("Kronson").email("anna@bth.se").city("Väsjö").phon("0760552135")
                .status(CustomerStatus.ACTIVE).payCard("521-318-3213").userName("Anna").password("4444").passwordConfirm("4444").roles(roles).build();
        userrepo.save(c);
        Role role2=Role.builder().name(RoleName.ROLE_ADMIN).build();
        roleRepo.save(role2);
        roles.add(role2);
        c = User.builder().firstName("Reckard").lastName("Lion").email("rechard@bth.se").city("Lund").phon("0760761265")
                .status(CustomerStatus.ACTIVE).payCard("321-100-2589").userName("Reckard").password("admin").passwordConfirm("admin").roles(roles).build();
        userrepo.save(c);
        userrepo.findAll().forEach(System.out::println);
    }

    public boolean isAvailable(List<AvailableTime> availableTimes, AvailableTime rentalTime) {
        for (AvailableTime t : availableTimes) {
            if (rentalTime.getDate().getDayOfWeek().equals(t.getDay()))
                if ((rentalTime.getStart().isAfter(t.getStart()) || (rentalTime.getStart().equals(t.getStart())))
                        && ((rentalTime.getEnd().isBefore(t.getEnd())) || (rentalTime.getEnd().equals(t.getEnd())))) {
                    return true;
                }
        }
        return false;
    }

    public boolean isValidate(LocalDate lastRentalDate, AvailableTime rentalTime) {
        if (rentalTime.getDate().isAfter(lastRentalDate))
            return true;
        return false;
    }

    public void checkReservations(List<Reservation> reservations) {
        for (Reservation r : reservations) {
            for (AvailableTime t : r.getRentalTimes()) {
                if (t.getDate().equals(LocalDate.now()) && (r.getRentalResource().getStatus().equals(ResourceStatus.RESERVED)))
                    sendReminder(r);
            }
        }
    }

    private void sendReminder(Reservation res) {
        System.out.println("Hi " + res.getRenter().getFirstName() + " " + res.getRenter().getLastName() + " this is time to return " + res.getRentalResource().getName());
    }
//    @PostConstruct
//    void init() {
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC+1"));
//    }

}