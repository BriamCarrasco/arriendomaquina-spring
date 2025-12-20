package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class UserInfoTest {

    private UserInfo userInfo;
    private User user;

    @BeforeEach
    void setUp() {
        userInfo = new UserInfo();
        user = new User();
    }

    @Test
    void testGetSetId() {
        long id = 1L;
        userInfo.setId(id);
        assertEquals(id, userInfo.getId());
    }

    @Test
    void testGetSetFirstName() {
        String firstName = "Juan";
        userInfo.setFirstName(firstName);
        assertEquals(firstName, userInfo.getFirstName());
    }

    @Test
    void testGetSetLastName() {
        String lastName = "Pérez";
        userInfo.setLastName(lastName);
        assertEquals(lastName, userInfo.getLastName());
    }

    @Test
    void testGetSetPhoneNumber() {
        String phoneNumber = "+56912345678";
        userInfo.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, userInfo.getPhoneNumber());
    }

    @Test
    void testGetSetAddress() {
        String address = "Av. Principal 123, Depto 456";
        userInfo.setAddress(address);
        assertEquals(address, userInfo.getAddress());
    }

    @Test
    void testGetSetCity() {
        String city = "Santiago";
        userInfo.setCity(city);
        assertEquals(city, userInfo.getCity());
    }

    @Test
    void testGetSetCountry() {
        String country = "Chile";
        userInfo.setCountry(country);
        assertEquals(country, userInfo.getCountry());
    }

    @Test
    void testGetSetUser() {
        user.setId(1L);
        user.setUsername("testuser");
        userInfo.setUser(user);

        assertNotNull(userInfo.getUser());
        assertEquals(user, userInfo.getUser());
        assertEquals("testuser", userInfo.getUser().getUsername());
    }

    @Test
    void testEqualsAndHashCode() {
        UserInfo info1 = new UserInfo();
        info1.setId(1L);
        info1.setFirstName("Juan");
        info1.setLastName("Pérez");

        UserInfo info2 = new UserInfo();
        info2.setId(1L);
        info2.setFirstName("Juan");
        info2.setLastName("Pérez");

        assertEquals(info1, info2);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void testNotEquals() {
        UserInfo info1 = new UserInfo();
        info1.setId(1L);
        info1.setFirstName("Juan");

        UserInfo info2 = new UserInfo();
        info2.setId(2L);
        info2.setFirstName("María");

        assertNotEquals(info1, info2);
    }

    @Test
    void testToString() {
        userInfo.setId(1L);
        userInfo.setFirstName("Carlos");
        userInfo.setLastName("González");

        String toString = userInfo.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("UserInfo"));
    }

    @Test
    void testCreateWithAllFields() {
        UserInfo info = new UserInfo();
        info.setId(10L);
        info.setFirstName("María");
        info.setLastName("Rodríguez");
        info.setPhoneNumber("+56987654321");
        info.setAddress("Calle Falsa 123");
        info.setCity("Valparaíso");
        info.setCountry("Chile");

        User testUser = new User();
        testUser.setId(5L);
        testUser.setUsername("maria.rodriguez");
        info.setUser(testUser);

        assertEquals(10L, info.getId());
        assertEquals("María", info.getFirstName());
        assertEquals("Rodríguez", info.getLastName());
        assertEquals("+56987654321", info.getPhoneNumber());
        assertEquals("Calle Falsa 123", info.getAddress());
        assertEquals("Valparaíso", info.getCity());
        assertEquals("Chile", info.getCountry());
        assertNotNull(info.getUser());
        assertEquals(5L, info.getUser().getId());
    }

    @Test
    void testPhoneNumberFormats() {
        String[] phoneFormats = {
                "+56912345678",
                "912345678",
                "+1-555-123-4567",
                "(02) 2345-6789"
        };

        for (String phone : phoneFormats) {
            userInfo.setPhoneNumber(phone);
            assertEquals(phone, userInfo.getPhoneNumber());
        }
    }

    @Test
    void testLongAddress() {
        String longAddress = "Avenida Libertador Bernardo O'Higgins 1234, Edificio Torre Central, Piso 15, Oficina 1501";
        userInfo.setAddress(longAddress);
        assertEquals(longAddress, userInfo.getAddress());
    }

    @Test
    void testNameWithAccents() {
        userInfo.setFirstName("José");
        userInfo.setLastName("Gutiérrez");

        assertEquals("José", userInfo.getFirstName());
        assertEquals("Gutiérrez", userInfo.getLastName());
    }

    @Test
    void testUserRelationship() {
        User u = new User();
        u.setId(100L);
        u.setUsername("jperez");
        u.setEmail("jperez@example.com");

        userInfo.setUser(u);

        assertNotNull(userInfo.getUser());
        assertEquals(100L, userInfo.getUser().getId());
        assertEquals("jperez", userInfo.getUser().getUsername());
        assertEquals("jperez@example.com", userInfo.getUser().getEmail());
    }

    @Test
    void testInitialValues() {
        UserInfo newInfo = new UserInfo();
        assertEquals(0L, newInfo.getId());
        assertNull(newInfo.getFirstName());
        assertNull(newInfo.getLastName());
        assertNull(newInfo.getPhoneNumber());
        assertNull(newInfo.getAddress());
        assertNull(newInfo.getCity());
        assertNull(newInfo.getCountry());
        assertNull(newInfo.getUser());
    }

    @Test
    void testCompleteName() {
        userInfo.setFirstName("Pedro");
        userInfo.setLastName("Sánchez");

        String completeName = userInfo.getFirstName() + " " + userInfo.getLastName();
        assertEquals("Pedro Sánchez", completeName);
    }

    @Test
    void testDifferentCountries() {
        String[] countries = { "Chile", "Argentina", "Perú", "Colombia", "México" };

        for (String country : countries) {
            userInfo.setCountry(country);
            assertEquals(country, userInfo.getCountry());
        }
    }

    @Test
    void testDifferentCities() {
        String[] cities = { "Santiago", "Concepción", "Valparaíso", "La Serena", "Antofagasta" };

        for (String city : cities) {
            userInfo.setCity(city);
            assertEquals(city, userInfo.getCity());
        }
    }

    @Test
    void testModifyAllFields() {
        userInfo.setFirstName("Original");
        userInfo.setLastName("Name");

        assertEquals("Original", userInfo.getFirstName());

        userInfo.setFirstName("Modified");
        assertEquals("Modified", userInfo.getFirstName());
        assertNotEquals("Original", userInfo.getFirstName());
    }

    @Test
    void testAddressWithSpecialCharacters() {
        String address = "Av. O'Higgins #456, Depto. 7-B";
        userInfo.setAddress(address);
        assertEquals(address, userInfo.getAddress());
    }

    @Test
    void testShortNames() {
        userInfo.setFirstName("Li");
        userInfo.setLastName("Wu");

        assertEquals("Li", userInfo.getFirstName());
        assertEquals("Wu", userInfo.getLastName());
        assertTrue(userInfo.getFirstName().length() <= 50);
        assertTrue(userInfo.getLastName().length() <= 50);
    }

    @Test
    void testMaxLengthFields() {
        String maxFirstName = "A".repeat(50);
        String maxLastName = "B".repeat(50);
        String maxPhone = "1".repeat(15);
        String maxAddress = "C".repeat(100);
        String maxCity = "D".repeat(50);
        String maxCountry = "E".repeat(50);

        userInfo.setFirstName(maxFirstName);
        userInfo.setLastName(maxLastName);
        userInfo.setPhoneNumber(maxPhone);
        userInfo.setAddress(maxAddress);
        userInfo.setCity(maxCity);
        userInfo.setCountry(maxCountry);

        assertEquals(50, userInfo.getFirstName().length());
        assertEquals(50, userInfo.getLastName().length());
        assertEquals(15, userInfo.getPhoneNumber().length());
        assertEquals(100, userInfo.getAddress().length());
        assertEquals(50, userInfo.getCity().length());
        assertEquals(50, userInfo.getCountry().length());
    }
}
