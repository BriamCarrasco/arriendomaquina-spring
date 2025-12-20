package com.briamcarrasco.arriendomaquinaria.model;

import com.briamcarrasco.arriendomaquinaria.model.User.Role;
import com.briamcarrasco.arriendomaquinaria.repository.CategoryRepository;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRentalRepository;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import com.briamcarrasco.arriendomaquinaria.repository.StatusRepository;
import com.briamcarrasco.arriendomaquinaria.repository.UserInfoRepository;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

/**
 * Componente que inicializa datos de ejemplo en la base de datos al iniciar la
 * aplicación.
 * Crea estados, categorías, usuarios, información adicional de usuarios,
 * maquinarias y arriendos de maquinaria si no existen.
 * Los métodos getters y setters para los atributos de las entidades
 * relacionadas se generan automáticamente mediante la anotación @Data de
 * Lombok.
 */
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

     // Constantes para evitar duplicación de literales
     private static final String STATUS_DISPONIBLE = "disponible";
     private static final String STATUS_ARRENDADA = "arrendada";
     private static final String CATEGORY_TRACTORES = "Tractores";
     private static final String CATEGORY_COSECHADORAS = "Cosechadoras";
     private static final String USERNAME_ADMIN = "admin";
     private static final String USERNAME_USER1 = "user1";
     private static final String USERNAME_USER2 = "user2";
     private static final String DEFAULT_PASSWORD = "password";
     private static final String MACHINERY_JOHN_DEERE = "John Deere 5075E Tractor";
     private static final String MACHINERY_CASE_IH = "Case IH Axial-Flow 7150 Combine";
     private static final String MACHINERY_NEW_HOLLAND = "New Holland CR8.90 Combine";

     private final UserRepository userRepository;

     private final UserInfoRepository userInfoRepository;

     private final PasswordEncoder passwordEncoder;

     private final CategoryRepository categoryRepository;

     private final StatusRepository statusRepository;

     private final MachineryRepository machineryRepository;

     private final MachineryRentalRepository machineryRentalRepository;

     public DataInitializer(UserRepository userRepository, UserInfoRepository userInfoRepository,
               PasswordEncoder passwordEncoder, CategoryRepository categoryRepository,
               StatusRepository statusRepository, MachineryRepository machineryRepository,
               MachineryRentalRepository machineryRentalRepository) {
          this.userRepository = userRepository;
          this.userInfoRepository = userInfoRepository;
          this.passwordEncoder = passwordEncoder;
          this.categoryRepository = categoryRepository;
          this.statusRepository = statusRepository;
          this.machineryRepository = machineryRepository;
          this.machineryRentalRepository = machineryRentalRepository;
     }

     @Override
     public void run(String... args) {
          Status disponible = statusRepository.findByName(STATUS_DISPONIBLE);
          if (disponible == null) {
               disponible = new Status();
               disponible.setName(STATUS_DISPONIBLE);
               statusRepository.save(disponible);
          }
          Status arrendada = statusRepository.findByName(STATUS_ARRENDADA);
          if (arrendada == null) {
               arrendada = new Status();
               arrendada.setName(STATUS_ARRENDADA);
               statusRepository.save(arrendada);
          }

          Category tractores = categoryRepository.findByName(CATEGORY_TRACTORES);
          if (tractores == null) {
               tractores = new Category();
               tractores.setName(CATEGORY_TRACTORES);
               tractores.setDescription("Maquinaria para labores de tiro y transporte");
               categoryRepository.save(tractores);
          }
          Category cosechadoras = categoryRepository.findByName(CATEGORY_COSECHADORAS);
          if (cosechadoras == null) {
               cosechadoras = new Category();
               cosechadoras.setName(CATEGORY_COSECHADORAS);
               cosechadoras.setDescription("Maquinaria para cosecha de granos");
               categoryRepository.save(cosechadoras);
          }

          if (userRepository.findByUsername(USERNAME_ADMIN) == null) {
               User admin = new User();
               admin.setUsername(USERNAME_ADMIN);
               admin.setEmail("admin@demo.com");
               admin.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
               admin.setRole(Role.ADMIN);
               userRepository.save(admin);
               createUserInfo(admin, "Ana", "Admin", "+56911111111", "Av. Central 123", "Santiago", "Chile");
          }
          if (userRepository.findByUsername(USERNAME_USER1) == null) {
               User user1 = new User();
               user1.setUsername(USERNAME_USER1);
               user1.setEmail("user1@demo.com");
               user1.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
               user1.setRole(Role.USER);
               userRepository.save(user1);
               createUserInfo(user1, "Bruno", "Campos", "+56922222222", "Calle Norte 456", "Valparaíso", "Chile");
          }
          if (userRepository.findByUsername(USERNAME_USER2) == null) {
               User user2 = new User();
               user2.setUsername(USERNAME_USER2);
               user2.setEmail("user2@demo.com");
               user2.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
               user2.setRole(Role.USER);
               userRepository.save(user2);
               createUserInfo(user2, "Carla", "Rojas", "+56933333333", "Pasaje Sur 789", "Concepción", "Chile");
          }

          if (!machineryRepository.existsByNameMachinery(MACHINERY_JOHN_DEERE)) {
               MachineryInfo info = new MachineryInfo();
               info.setDescription("Tractor utilitario 75 HP, transmisión PowrReverser 12x12");
               Machinery m = new Machinery();
               m.setNameMachinery(MACHINERY_JOHN_DEERE);
               m.setStatus(STATUS_DISPONIBLE);
               m.setPricePerDay(new BigDecimal("150.00"));
               m.setCategory(tractores);
               m.setMachineryInfo(info);
               m.setImageUrl("/images/john_deere_5075e.png");
               machineryRepository.save(m);
          }
          if (!machineryRepository.existsByNameMachinery(MACHINERY_CASE_IH)) {
               MachineryInfo info = new MachineryInfo();
               info.setDescription("Cosechadora Axial-Flow 7150, motor FPT Cursor 11, rotor único");
               Machinery m = new Machinery();
               m.setNameMachinery(MACHINERY_CASE_IH);
               m.setStatus(STATUS_DISPONIBLE);
               m.setPricePerDay(new BigDecimal("350.00"));
               m.setCategory(cosechadoras);
               m.setMachineryInfo(info);
               m.setImageUrl("/images/Case_IH_Axial-Flow.png");
               machineryRepository.save(m);
          }
          if (!machineryRepository.existsByNameMachinery(MACHINERY_NEW_HOLLAND)) {
               MachineryInfo info = new MachineryInfo();
               info.setDescription("Cosechadora CR8.90 Twin Rotor, alta capacidad en cereales");
               Machinery m = new Machinery();
               m.setNameMachinery(MACHINERY_NEW_HOLLAND);
               m.setStatus(STATUS_ARRENDADA);
               m.setPricePerDay(new BigDecimal("400.00"));
               m.setCategory(cosechadoras);
               m.setMachineryInfo(info);
               m.setImageUrl("/images/new_holland_cr890.png");
               machineryRepository.save(m);
          }

          if (machineryRentalRepository.count() == 0) {
               User u1 = userRepository.findByUsername(USERNAME_USER1);
               User u2 = userRepository.findByUsername(USERNAME_USER2);
               User admin = userRepository.findByUsername(USERNAME_ADMIN);
               Machinery jd = machineryRepository.findByNameMachinery(MACHINERY_JOHN_DEERE);
               Machinery caseIH = machineryRepository.findByNameMachinery(MACHINERY_CASE_IH);
               Machinery nh = machineryRepository.findByNameMachinery(MACHINERY_NEW_HOLLAND);

               MachineryRental r1 = new MachineryRental();
               r1.setMachinery(nh);
               r1.setUser(u1);
               r1.setRentalDate(todayPlusDays(0));
               r1.setReturnDate(todayPlusDays(7));
               machineryRentalRepository.save(r1);

               MachineryRental r2 = new MachineryRental();
               r2.setMachinery(jd);
               r2.setUser(u2);
               r2.setRentalDate(todayPlusDays(-10));
               r2.setReturnDate(todayPlusDays(-3));
               machineryRentalRepository.save(r2);

               MachineryRental r3 = new MachineryRental();
               r3.setMachinery(caseIH);
               r3.setUser(admin);
               r3.setRentalDate(todayPlusDays(-2));
               r3.setReturnDate(todayPlusDays(5));
               machineryRentalRepository.save(r3);
          }
     }

     private void createUserInfo(User user, String first, String last, String phone, String address, String city,
               String country) {
          if (!userInfoRepository.existsByUser(user)) {
               UserInfo ui = new UserInfo();
               ui.setUser(user);
               ui.setFirstName(first);
               ui.setLastName(last);
               ui.setPhoneNumber(phone);
               ui.setAddress(address);
               ui.setCity(city);
               ui.setCountry(country);
               userInfoRepository.save(ui);
          }
     }

     private Date todayPlusDays(int days) {
          Calendar cal = Calendar.getInstance();
          cal.add(Calendar.DAY_OF_MONTH, days);
          return cal.getTime();
     }
}