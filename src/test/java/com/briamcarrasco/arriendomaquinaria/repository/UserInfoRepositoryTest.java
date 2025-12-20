package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.UserInfo;
import com.briamcarrasco.arriendomaquinaria.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveUserInfo() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Juan");
        userInfo.setLastName("Pérez");
        userInfo.setPhoneNumber("+56912345678");
        userInfo.setAddress("Av. Principal 123");
        userInfo.setCity("Santiago");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        assertNotNull(savedUserInfo);
        assertNotNull(savedUserInfo.getId());
        assertEquals("Juan", savedUserInfo.getFirstName());
        assertEquals("Pérez", savedUserInfo.getLastName());
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setUsername("finduser");
        user.setPassword("pass123");
        user.setEmail("find@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("María");
        userInfo.setLastName("González");
        userInfo.setPhoneNumber("+56987654321");
        userInfo.setAddress("Calle Falsa 456");
        userInfo.setCity("Valparaíso");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);
        UserInfo savedUserInfo = entityManager.persistAndFlush(userInfo);

        Optional<UserInfo> foundUserInfo = userInfoRepository.findById(savedUserInfo.getId());

        assertTrue(foundUserInfo.isPresent());
        assertEquals(savedUserInfo.getId(), foundUserInfo.get().getId());
        assertEquals("María", foundUserInfo.get().getFirstName());
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setUsername("byuser");
        user.setPassword("bypass");
        user.setEmail("byuser@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Carlos");
        userInfo.setLastName("Rodríguez");
        userInfo.setPhoneNumber("+56923456789");
        userInfo.setAddress("Pasaje Los Olivos 789");
        userInfo.setCity("Concepción");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);
        entityManager.persistAndFlush(userInfo);

        UserInfo foundUserInfo = userInfoRepository.findByUser(user);

        assertNotNull(foundUserInfo);
        assertEquals("Carlos", foundUserInfo.getFirstName());
        assertEquals(user.getId(), foundUserInfo.getUser().getId());
    }

    @Test
    void testFindByUserReturnsNull() {
        User user = new User();
        user.setUsername("noinfo");
        user.setPassword("nopass");
        user.setEmail("noinfo@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo foundUserInfo = userInfoRepository.findByUser(user);
        assertNull(foundUserInfo);
    }

    @Test
    void testExistsByUser() {
        User user = new User();
        user.setUsername("existsuser");
        user.setPassword("existspass");
        user.setEmail("exists@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Ana");
        userInfo.setLastName("Martínez");
        userInfo.setPhoneNumber("+56934567890");
        userInfo.setAddress("Av. Libertad 321");
        userInfo.setCity("La Serena");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);
        entityManager.persistAndFlush(userInfo);

        boolean exists = userInfoRepository.existsByUser(user);
        assertTrue(exists);
    }

    @Test
    void testExistsByUserReturnsFalse() {
        User user = new User();
        user.setUsername("noexists");
        user.setPassword("noexistspass");
        user.setEmail("noexists@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        boolean exists = userInfoRepository.existsByUser(user);
        assertFalse(exists);
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

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setFirstName("Pedro");
        userInfo1.setLastName("Sánchez");
        userInfo1.setPhoneNumber("+56945678901");
        userInfo1.setAddress("Calle 1");
        userInfo1.setCity("Antofagasta");
        userInfo1.setCountry("Chile");
        userInfo1.setUser(user1);
        entityManager.persistAndFlush(userInfo1);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setFirstName("Laura");
        userInfo2.setLastName("Fernández");
        userInfo2.setPhoneNumber("+56956789012");
        userInfo2.setAddress("Calle 2");
        userInfo2.setCity("Iquique");
        userInfo2.setCountry("Chile");
        userInfo2.setUser(user2);
        entityManager.persistAndFlush(userInfo2);

        List<UserInfo> allUserInfo = userInfoRepository.findAll();

        assertNotNull(allUserInfo);
        assertTrue(allUserInfo.size() >= 2);
    }

    @Test
    void testDeleteUserInfo() {
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword("deletepass");
        user.setEmail("delete@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Diego");
        userInfo.setLastName("López");
        userInfo.setPhoneNumber("+56967890123");
        userInfo.setAddress("Av. Delete 999");
        userInfo.setCity("Temuco");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);
        UserInfo savedUserInfo = entityManager.persistAndFlush(userInfo);

        userInfoRepository.deleteById(savedUserInfo.getId());

        Optional<UserInfo> deletedUserInfo = userInfoRepository.findById(savedUserInfo.getId());
        assertFalse(deletedUserInfo.isPresent());
    }

    @Test
    void testCount() {
        User user = new User();
        user.setUsername("countuser");
        user.setPassword("countpass");
        user.setEmail("count@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        long countBefore = userInfoRepository.count();

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Sofía");
        userInfo.setLastName("Torres");
        userInfo.setPhoneNumber("+56978901234");
        userInfo.setAddress("Pasaje Count 555");
        userInfo.setCity("Valdivia");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);
        entityManager.persistAndFlush(userInfo);

        long countAfter = userInfoRepository.count();

        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void testExistsById() {
        User user = new User();
        user.setUsername("existsid");
        user.setPassword("existsidpass");
        user.setEmail("existsid@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Ricardo");
        userInfo.setLastName("Herrera");
        userInfo.setPhoneNumber("+56989012345");
        userInfo.setAddress("Calle Exists 777");
        userInfo.setCity("Puerto Montt");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);
        UserInfo savedUserInfo = entityManager.persistAndFlush(userInfo);

        assertTrue(userInfoRepository.existsById(savedUserInfo.getId()));
        assertFalse(userInfoRepository.existsById(999L));
    }

    @Test
    void testUpdateUserInfo() {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("updatepass");
        user.setEmail("update@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("Original");
        userInfo.setLastName("Name");
        userInfo.setPhoneNumber("+56900000000");
        userInfo.setAddress("Old Address");
        userInfo.setCity("Old City");
        userInfo.setCountry("Old Country");
        userInfo.setUser(user);
        UserInfo savedUserInfo = entityManager.persistAndFlush(userInfo);

        savedUserInfo.setFirstName("Updated");
        savedUserInfo.setLastName("NewName");
        savedUserInfo.setCity("New City");
        UserInfo updatedUserInfo = userInfoRepository.save(savedUserInfo);

        assertEquals("Updated", updatedUserInfo.getFirstName());
        assertEquals("NewName", updatedUserInfo.getLastName());
        assertEquals("New City", updatedUserInfo.getCity());
    }

    @Test
    void testUserInfoWithLongFields() {
        User user = new User();
        user.setUsername("longuser");
        user.setPassword("longpass");
        user.setEmail("long@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        String longAddress = "Avenida Libertador Bernardo O'Higgins 1234, Edificio Torre Central";

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("NombreMuyLargo");
        userInfo.setLastName("ApellidoMuyLargo");
        userInfo.setPhoneNumber("+561234567890");
        userInfo.setAddress(longAddress);
        userInfo.setCity("Santiago Centro");
        userInfo.setCountry("República de Chile");
        userInfo.setUser(user);

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        assertEquals(longAddress, savedUserInfo.getAddress());
    }

    @Test
    void testUserInfoWithSpecialCharacters() {
        User user = new User();
        user.setUsername("specialuser");
        user.setPassword("specialpass");
        user.setEmail("special@example.com");
        user.setRole(User.Role.USER);
        entityManager.persistAndFlush(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName("José");
        userInfo.setLastName("Gutiérrez-López");
        userInfo.setPhoneNumber("+56 9 1234 5678");
        userInfo.setAddress("Av. O'Higgins #456");
        userInfo.setCity("Viña del Mar");
        userInfo.setCountry("Chile");
        userInfo.setUser(user);

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        assertEquals("José", savedUserInfo.getFirstName());
        assertEquals("Gutiérrez-López", savedUserInfo.getLastName());
        assertEquals("Av. O'Higgins #456", savedUserInfo.getAddress());
    }

    @Test
    void testMultipleUserInfoForDifferentUsers() {
        User user1 = new User();
        user1.setUsername("multi1");
        user1.setPassword("pass1");
        user1.setEmail("multi1@example.com");
        user1.setRole(User.Role.USER);
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setUsername("multi2");
        user2.setPassword("pass2");
        user2.setEmail("multi2@example.com");
        user2.setRole(User.Role.USER);
        entityManager.persistAndFlush(user2);

        UserInfo info1 = new UserInfo();
        info1.setFirstName("Usuario");
        info1.setLastName("Uno");
        info1.setPhoneNumber("+56911111111");
        info1.setAddress("Dirección 1");
        info1.setCity("Ciudad 1");
        info1.setCountry("País 1");
        info1.setUser(user1);
        entityManager.persistAndFlush(info1);

        UserInfo info2 = new UserInfo();
        info2.setFirstName("Usuario");
        info2.setLastName("Dos");
        info2.setPhoneNumber("+56922222222");
        info2.setAddress("Dirección 2");
        info2.setCity("Ciudad 2");
        info2.setCountry("País 2");
        info2.setUser(user2);
        entityManager.persistAndFlush(info2);

        UserInfo found1 = userInfoRepository.findByUser(user1);
        UserInfo found2 = userInfoRepository.findByUser(user2);

        assertNotNull(found1);
        assertNotNull(found2);
        assertNotEquals(found1.getId(), found2.getId());
        assertEquals("Uno", found1.getLastName());
        assertEquals("Dos", found2.getLastName());
    }

    @Test
    void testFindByIdReturnsEmpty() {
        Optional<UserInfo> userInfo = userInfoRepository.findById(999L);
        assertFalse(userInfo.isPresent());
    }

    @Test
    void testDeleteAllUserInfo() {
        User user1 = new User();
        user1.setUsername("deleteall1");
        user1.setPassword("pass1");
        user1.setEmail("deleteall1@example.com");
        user1.setRole(User.Role.USER);
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setUsername("deleteall2");
        user2.setPassword("pass2");
        user2.setEmail("deleteall2@example.com");
        user2.setRole(User.Role.USER);
        entityManager.persistAndFlush(user2);

        UserInfo info1 = new UserInfo();
        info1.setFirstName("Delete");
        info1.setLastName("One");
        info1.setPhoneNumber("+56900000001");
        info1.setAddress("Delete 1");
        info1.setCity("City 1");
        info1.setCountry("Country 1");
        info1.setUser(user1);
        UserInfo saved1 = entityManager.persistAndFlush(info1);

        UserInfo info2 = new UserInfo();
        info2.setFirstName("Delete");
        info2.setLastName("Two");
        info2.setPhoneNumber("+56900000002");
        info2.setAddress("Delete 2");
        info2.setCity("City 2");
        info2.setCountry("Country 2");
        info2.setUser(user2);
        UserInfo saved2 = entityManager.persistAndFlush(info2);

        userInfoRepository.deleteAll(List.of(saved1, saved2));

        assertFalse(userInfoRepository.existsById(saved1.getId()));
        assertFalse(userInfoRepository.existsById(saved2.getId()));
    }
}
