package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.MachineryRental;
import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class MachineryRentalRepositoryTest {

    @Autowired
    private MachineryRentalRepository machineryRentalRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveMachineryRental() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Construcción");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Excavadora");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("10000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        Date rentalDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date returnDate = cal.getTime();

        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(rentalDate);
        rental.setReturnDate(returnDate);
        rental.setMachinery(machinery);
        rental.setUser(user);

        MachineryRental savedRental = machineryRentalRepository.save(rental);

        assertNotNull(savedRental);
        assertNotNull(savedRental.getId());
        assertEquals(rentalDate, savedRental.getRentalDate());
        assertEquals(returnDate, savedRental.getReturnDate());
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setUsername("rentuser");
        user.setPassword("pass456");
        user.setEmail("rent@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Industrial");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Grúa");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("15000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(new Date());
        rental.setReturnDate(new Date());
        rental.setMachinery(machinery);
        rental.setUser(user);
        MachineryRental savedRental = entityManager.persistAndFlush(rental);

        Optional<MachineryRental> foundRental = machineryRentalRepository.findById(savedRental.getId());

        assertTrue(foundRental.isPresent());
        assertEquals(savedRental.getId(), foundRental.get().getId());
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass1");
        user1.setEmail("user1@example.com");
        user1.setRole(User.Role.USER);
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass2");
        user2.setEmail("user2@example.com");
        user2.setRole(User.Role.USER);
        entityManager.persistAndFlush(user2);

        Category category = new Category();
        category.setName("Transporte");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Montacargas");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("5000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryRental rental1 = new MachineryRental();
        rental1.setRentalDate(new Date());
        rental1.setReturnDate(new Date());
        rental1.setMachinery(machinery);
        rental1.setUser(user1);
        entityManager.persistAndFlush(rental1);

        MachineryRental rental2 = new MachineryRental();
        rental2.setRentalDate(new Date());
        rental2.setReturnDate(new Date());
        rental2.setMachinery(machinery);
        rental2.setUser(user2);
        entityManager.persistAndFlush(rental2);

        List<MachineryRental> allRentals = machineryRentalRepository.findAll();

        assertNotNull(allRentals);
        assertTrue(allRentals.size() >= 2);
    }

    @Test
    void testDeleteMachineryRental() {
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword("deletepass");
        user.setEmail("delete@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Minería");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Bulldozer");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("12000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(new Date());
        rental.setReturnDate(new Date());
        rental.setMachinery(machinery);
        rental.setUser(user);
        MachineryRental savedRental = entityManager.persistAndFlush(rental);

        machineryRentalRepository.deleteById(savedRental.getId());

        Optional<MachineryRental> deletedRental = machineryRentalRepository.findById(savedRental.getId());
        assertFalse(deletedRental.isPresent());
    }

    @Test
    void testCount() {
        User user = new User();
        user.setUsername("countuser");
        user.setPassword("countpass");
        user.setEmail("count@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Agricultura");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Tractor");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("8000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        long countBefore = machineryRentalRepository.count();

        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(new Date());
        rental.setReturnDate(new Date());
        rental.setMachinery(machinery);
        rental.setUser(user);
        entityManager.persistAndFlush(rental);

        long countAfter = machineryRentalRepository.count();

        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void testExistsById() {
        User user = new User();
        user.setUsername("existuser");
        user.setPassword("existpass");
        user.setEmail("exist@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Electricidad");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Generador");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("4000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(new Date());
        rental.setReturnDate(new Date());
        rental.setMachinery(machinery);
        rental.setUser(user);
        MachineryRental savedRental = entityManager.persistAndFlush(rental);

        assertTrue(machineryRentalRepository.existsById(savedRental.getId()));
        assertFalse(machineryRentalRepository.existsById(999L));
    }

    @Test
    void testUpdateMachineryRental() {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("updatepass");
        user.setEmail("update@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Pavimentación");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Compactadora");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("7000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        Date originalDate = new Date();
        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(originalDate);
        rental.setReturnDate(originalDate);
        rental.setMachinery(machinery);
        rental.setUser(user);
        MachineryRental savedRental = entityManager.persistAndFlush(rental);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date newReturnDate = cal.getTime();

        savedRental.setReturnDate(newReturnDate);
        MachineryRental updatedRental = machineryRentalRepository.save(savedRental);

        assertEquals(newReturnDate, updatedRental.getReturnDate());
    }

    @Test
    void testRentalWithDifferentDates() {
        User user = new User();
        user.setUsername("dateuser");
        user.setPassword("datepass");
        user.setEmail("date@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Jardinería");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Cortacésped");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("2000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        Calendar cal = Calendar.getInstance();
        Date rentalDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        Date returnDate = cal.getTime();

        MachineryRental rental = new MachineryRental();
        rental.setRentalDate(rentalDate);
        rental.setReturnDate(returnDate);
        rental.setMachinery(machinery);
        rental.setUser(user);

        MachineryRental savedRental = machineryRentalRepository.save(rental);

        assertTrue(savedRental.getReturnDate().after(savedRental.getRentalDate()));
    }

    @Test
    void testMultipleRentalsForSameUser() {
        User user = new User();
        user.setUsername("multiuser");
        user.setPassword("multipass");
        user.setEmail("multi@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Limpieza");
        entityManager.persistAndFlush(category);

        Machinery machinery1 = new Machinery();
        machinery1.setNameMachinery("Hidrolavadora");
        machinery1.setStatus("Disponible");
        machinery1.setPricePerDay(new BigDecimal("2500"));
        machinery1.setCategory(category);
        entityManager.persistAndFlush(machinery1);

        Machinery machinery2 = new Machinery();
        machinery2.setNameMachinery("Aspiradora Industrial");
        machinery2.setStatus("Disponible");
        machinery2.setPricePerDay(new BigDecimal("3000"));
        machinery2.setCategory(category);
        entityManager.persistAndFlush(machinery2);

        MachineryRental rental1 = new MachineryRental();
        rental1.setRentalDate(new Date());
        rental1.setReturnDate(new Date());
        rental1.setMachinery(machinery1);
        rental1.setUser(user);
        entityManager.persistAndFlush(rental1);

        MachineryRental rental2 = new MachineryRental();
        rental2.setRentalDate(new Date());
        rental2.setReturnDate(new Date());
        rental2.setMachinery(machinery2);
        rental2.setUser(user);
        entityManager.persistAndFlush(rental2);

        List<MachineryRental> allRentals = machineryRentalRepository.findAll();
        long count = allRentals.stream()
                .filter(r -> r.getUser().getId() == user.getId())
                .count();

        assertTrue(count >= 2L);
    }

    @Test
    void testMultipleRentalsForSameMachinery() {
        User user1 = new User();
        user1.setUsername("machuser1");
        user1.setPassword("pass1");
        user1.setEmail("machuser1@example.com");
        user1.setRole(User.Role.USER);
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setUsername("machuser2");
        user2.setPassword("pass2");
        user2.setEmail("machuser2@example.com");
        user2.setRole(User.Role.USER);
        entityManager.persistAndFlush(user2);

        Category category = new Category();
        category.setName("Demolición");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Martillo Hidráulico");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("9000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date date2 = cal.getTime();

        MachineryRental rental1 = new MachineryRental();
        rental1.setRentalDate(date1);
        rental1.setReturnDate(date1);
        rental1.setMachinery(machinery);
        rental1.setUser(user1);
        entityManager.persistAndFlush(rental1);

        MachineryRental rental2 = new MachineryRental();
        rental2.setRentalDate(date2);
        rental2.setReturnDate(date2);
        rental2.setMachinery(machinery);
        rental2.setUser(user2);
        entityManager.persistAndFlush(rental2);

        List<MachineryRental> allRentals = machineryRentalRepository.findAll();

        long machineryRentals = allRentals.stream()
                .filter(r -> r.getMachinery().getId() == machinery.getId())
                .count();

        assertTrue(machineryRentals >= 2L);
    }

    @Test
    void testFindByIdReturnsEmpty() {
        Optional<MachineryRental> rental = machineryRentalRepository.findById(999L);
        assertFalse(rental.isPresent());
    }

    @Test
    void testDeleteAll() {
        User user = new User();
        user.setUsername("deletealluser");
        user.setPassword("deleteallpass");
        user.setEmail("deleteall@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        Category category = new Category();
        category.setName("Test Category");
        entityManager.persistAndFlush(category);

        Machinery machinery = new Machinery();
        machinery.setNameMachinery("Test Machinery");
        machinery.setStatus("Disponible");
        machinery.setPricePerDay(new BigDecimal("1000"));
        machinery.setCategory(category);
        entityManager.persistAndFlush(machinery);

        MachineryRental rental1 = new MachineryRental();
        rental1.setRentalDate(new Date());
        rental1.setReturnDate(new Date());
        rental1.setMachinery(machinery);
        rental1.setUser(user);
        MachineryRental saved1 = entityManager.persistAndFlush(rental1);

        MachineryRental rental2 = new MachineryRental();
        rental2.setRentalDate(new Date());
        rental2.setReturnDate(new Date());
        rental2.setMachinery(machinery);
        rental2.setUser(user);
        MachineryRental saved2 = entityManager.persistAndFlush(rental2);

        machineryRentalRepository.deleteAll(List.of(saved1, saved2));

        assertFalse(machineryRentalRepository.existsById(saved1.getId()));
        assertFalse(machineryRentalRepository.existsById(saved2.getId()));
    }
}
