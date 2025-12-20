package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Calendar;

class MachineryRentalTest {

    private MachineryRental machineryRental;
    private Machinery machinery;
    private User user;
    private Date rentalDate;
    private Date returnDate;

    @BeforeEach
    void setUp() {
        machineryRental = new MachineryRental();
        machinery = new Machinery();
        user = new User();

        Calendar cal = Calendar.getInstance();
        rentalDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        returnDate = cal.getTime();
    }

    @Test
    void testGetSetId() {
        Long id = 1L;
        machineryRental.setId(id);
        assertEquals(id, machineryRental.getId());
    }

    @Test
    void testGetSetRentalDate() {
        machineryRental.setRentalDate(rentalDate);
        assertEquals(rentalDate, machineryRental.getRentalDate());
    }

    @Test
    void testGetSetReturnDate() {
        machineryRental.setReturnDate(returnDate);
        assertEquals(returnDate, machineryRental.getReturnDate());
    }

    @Test
    void testGetSetMachinery() {
        machinery.setId(1L);
        machinery.setNameMachinery("Excavadora");
        machineryRental.setMachinery(machinery);

        assertNotNull(machineryRental.getMachinery());
        assertEquals(machinery, machineryRental.getMachinery());
        assertEquals("Excavadora", machineryRental.getMachinery().getNameMachinery());
    }

    @Test
    void testGetSetUser() {
        user.setId(1L);
        user.setUsername("testuser");
        machineryRental.setUser(user);

        assertNotNull(machineryRental.getUser());
        assertEquals(user, machineryRental.getUser());
        assertEquals("testuser", machineryRental.getUser().getUsername());
    }

    @Test
    void testEqualsAndHashCode() {
        MachineryRental rental1 = new MachineryRental();
        rental1.setId(1L);
        rental1.setRentalDate(rentalDate);
        rental1.setReturnDate(returnDate);

        MachineryRental rental2 = new MachineryRental();
        rental2.setId(1L);
        rental2.setRentalDate(rentalDate);
        rental2.setReturnDate(returnDate);

        assertEquals(rental1, rental2);
        assertEquals(rental1.hashCode(), rental2.hashCode());
    }

    @Test
    void testNotEquals() {
        MachineryRental rental1 = new MachineryRental();
        rental1.setId(1L);
        rental1.setRentalDate(rentalDate);

        MachineryRental rental2 = new MachineryRental();
        rental2.setId(2L);
        rental2.setRentalDate(returnDate);

        assertNotEquals(rental1, rental2);
    }

    @Test
    void testToString() {
        machineryRental.setId(1L);
        machineryRental.setRentalDate(rentalDate);
        machineryRental.setReturnDate(returnDate);

        String toString = machineryRental.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("MachineryRental"));
    }

    @Test
    void testCreateWithAllFields() {
        MachineryRental rental = new MachineryRental();
        rental.setId(10L);
        rental.setRentalDate(rentalDate);
        rental.setReturnDate(returnDate);

        Machinery testMachinery = new Machinery();
        testMachinery.setId(5L);
        testMachinery.setNameMachinery("Bulldozer");
        rental.setMachinery(testMachinery);

        User testUser = new User();
        testUser.setId(3L);
        testUser.setUsername("rentuser");
        rental.setUser(testUser);

        assertEquals(10L, rental.getId());
        assertEquals(rentalDate, rental.getRentalDate());
        assertEquals(returnDate, rental.getReturnDate());
        assertNotNull(rental.getMachinery());
        assertEquals(5L, rental.getMachinery().getId());
        assertNotNull(rental.getUser());
        assertEquals(3L, rental.getUser().getId());
    }

    @Test
    void testRentalPeriod() {
        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date end = cal.getTime();

        machineryRental.setRentalDate(start);
        machineryRental.setReturnDate(end);

        assertTrue(machineryRental.getReturnDate().after(machineryRental.getRentalDate()));
    }

    @Test
    void testSameDayRental() {
        Date today = new Date();
        machineryRental.setRentalDate(today);
        machineryRental.setReturnDate(today);

        assertEquals(machineryRental.getRentalDate(), machineryRental.getReturnDate());
    }

    @Test
    void testLongTermRental() {
        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 6);
        Date end = cal.getTime();

        machineryRental.setRentalDate(start);
        machineryRental.setReturnDate(end);

        assertTrue(machineryRental.getReturnDate().after(machineryRental.getRentalDate()));
        assertNotNull(machineryRental.getRentalDate());
        assertNotNull(machineryRental.getReturnDate());
    }

    @Test
    void testMachineryRelationship() {
        Machinery m = new Machinery();
        m.setId(100L);
        m.setNameMachinery("Grúa Torre");
        m.setStatus("Arrendada");

        machineryRental.setMachinery(m);

        assertNotNull(machineryRental.getMachinery());
        assertEquals(100L, machineryRental.getMachinery().getId());
        assertEquals("Grúa Torre", machineryRental.getMachinery().getNameMachinery());
        assertEquals("Arrendada", machineryRental.getMachinery().getStatus());
    }

    @Test
    void testUserRelationship() {
        User u = new User();
        u.setId(50L);
        u.setUsername("contractor123");
        u.setEmail("contractor@example.com");

        machineryRental.setUser(u);

        assertNotNull(machineryRental.getUser());
        assertEquals(50L, machineryRental.getUser().getId());
        assertEquals("contractor123", machineryRental.getUser().getUsername());
        assertEquals("contractor@example.com", machineryRental.getUser().getEmail());
    }

    @Test
    void testInitialValues() {
        MachineryRental newRental = new MachineryRental();
        assertNull(newRental.getId());
        assertNull(newRental.getRentalDate());
        assertNull(newRental.getReturnDate());
        assertNull(newRental.getMachinery());
        assertNull(newRental.getUser());
    }

    @Test
    void testMultipleRentalsForSameMachinery() {
        MachineryRental rental1 = new MachineryRental();
        rental1.setId(1L);
        rental1.setMachinery(machinery);
        rental1.setRentalDate(rentalDate);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        Date futureDate = cal.getTime();

        MachineryRental rental2 = new MachineryRental();
        rental2.setId(2L);
        rental2.setMachinery(machinery);
        rental2.setRentalDate(futureDate);

        assertNotEquals(rental1.getId(), rental2.getId());
        assertEquals(rental1.getMachinery(), rental2.getMachinery());
        assertNotEquals(rental1.getRentalDate(), rental2.getRentalDate());
    }

    @Test
    void testMultipleRentalsForSameUser() {
        Machinery m1 = new Machinery();
        m1.setId(1L);
        m1.setNameMachinery("Excavadora");

        Machinery m2 = new Machinery();
        m2.setId(2L);
        m2.setNameMachinery("Bulldozer");

        MachineryRental rental1 = new MachineryRental();
        rental1.setId(1L);
        rental1.setUser(user);
        rental1.setMachinery(m1);

        MachineryRental rental2 = new MachineryRental();
        rental2.setId(2L);
        rental2.setUser(user);
        rental2.setMachinery(m2);

        assertNotEquals(rental1.getId(), rental2.getId());
        assertEquals(rental1.getUser(), rental2.getUser());
        assertNotEquals(rental1.getMachinery(), rental2.getMachinery());
    }

    @Test
    void testDateModification() {
        Date originalDate = new Date();
        machineryRental.setRentalDate(originalDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date newDate = cal.getTime();

        machineryRental.setRentalDate(newDate);

        assertEquals(newDate, machineryRental.getRentalDate());
        assertNotEquals(originalDate, machineryRental.getRentalDate());
    }
}
