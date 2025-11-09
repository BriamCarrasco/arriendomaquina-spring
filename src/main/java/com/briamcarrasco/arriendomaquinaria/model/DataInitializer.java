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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


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
public class DataInitializer implements CommandLineRunner {

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private UserInfoRepository userInfoRepository;

     @Autowired
     private PasswordEncoder passwordEncoder;

     @Autowired
     private CategoryRepository categoryRepository;

     @Autowired
     private StatusRepository statusRepository;

     @Autowired
     private MachineryRepository machineryRepository;

     @Autowired
     private MachineryRentalRepository machineryRentalRepository;

     @Override
     public void run(String... args) {
          Status disponible = statusRepository.findByName("disponible");
          if (disponible == null) {
               disponible = new Status();
               disponible.setName("disponible");
               statusRepository.save(disponible);
          }
          Status arrendada = statusRepository.findByName("arrendada");
          if (arrendada == null) {
               arrendada = new Status();
               arrendada.setName("arrendada");
               statusRepository.save(arrendada);
          }

          Category tractores = categoryRepository.findByName("Tractores");
          if (tractores == null) {
               tractores = new Category();
               tractores.setName("Tractores");
               tractores.setDescription("Maquinaria para labores de tiro y transporte");
               categoryRepository.save(tractores);
          }
          Category cosechadoras = categoryRepository.findByName("Cosechadoras");
          if (cosechadoras == null) {
               cosechadoras = new Category();
               cosechadoras.setName("Cosechadoras");
               cosechadoras.setDescription("Maquinaria para cosecha de granos");
               categoryRepository.save(cosechadoras);
          }

          if (userRepository.findByUsername("admin") == null) {
               User admin = new User();
               admin.setUsername("admin");
               admin.setEmail("admin@demo.com");
               admin.setPassword(passwordEncoder.encode("password"));
               admin.setRole(Role.ADMIN);
               userRepository.save(admin);
               createUserInfo(admin, "Ana", "Admin", "+56911111111", "Av. Central 123", "Santiago", "Chile");
          }
          if (userRepository.findByUsername("user1") == null) {
               User user1 = new User();
               user1.setUsername("user1");
               user1.setEmail("user1@demo.com");
               user1.setPassword(passwordEncoder.encode("password"));
               user1.setRole(Role.USER);
               userRepository.save(user1);
               createUserInfo(user1, "Bruno", "Campos", "+56922222222", "Calle Norte 456", "Valparaíso", "Chile");
          }
          if (userRepository.findByUsername("user2") == null) {
               User user2 = new User();
               user2.setUsername("user2");
               user2.setEmail("user2@demo.com");
               user2.setPassword(passwordEncoder.encode("password"));
               user2.setRole(Role.USER);
               userRepository.save(user2);
               createUserInfo(user2, "Carla", "Rojas", "+56933333333", "Pasaje Sur 789", "Concepción", "Chile");
          }

          if (!machineryRepository.existsByNameMachinery("John Deere 5075E Tractor")) {
               MachineryInfo info = new MachineryInfo();
               info.setDescription("Tractor utilitario 75 HP, transmisión PowrReverser 12x12");
               Machinery m = new Machinery();
               m.setNameMachinery("John Deere 5075E Tractor");
               m.setStatus("disponible");
               m.setPricePerDay(new BigDecimal("150.00"));
               m.setCategory(tractores);
               m.setMachineryInfo(info);
               machineryRepository.save(m);
          }
          if (!machineryRepository.existsByNameMachinery("Case IH Axial-Flow 7150 Combine")) {
               MachineryInfo info = new MachineryInfo();
               info.setDescription("Cosechadora Axial-Flow 7150, motor FPT Cursor 11, rotor único");
               Machinery m = new Machinery();
               m.setNameMachinery("Case IH Axial-Flow 7150 Combine");
               m.setStatus("disponible");
               m.setPricePerDay(new BigDecimal("350.00"));
               m.setCategory(cosechadoras);
               m.setMachineryInfo(info);
               machineryRepository.save(m);
          }
          if (!machineryRepository.existsByNameMachinery("New Holland CR8.90 Combine")) {
               MachineryInfo info = new MachineryInfo();
               info.setDescription("Cosechadora CR8.90 Twin Rotor, alta capacidad en cereales");
               Machinery m = new Machinery();
               m.setNameMachinery("New Holland CR8.90 Combine");
               m.setStatus("arrendada");
               m.setPricePerDay(new BigDecimal("400.00"));
               m.setCategory(cosechadoras);
               m.setMachineryInfo(info);
               machineryRepository.save(m);
          }

          if (machineryRentalRepository.count() == 0) {
               User u1 = userRepository.findByUsername("user1");
               User u2 = userRepository.findByUsername("user2");
               User admin = userRepository.findByUsername("admin");
               Machinery jd = machineryRepository.findByNameMachinery("John Deere 5075E Tractor");
               Machinery caseIH = machineryRepository.findByNameMachinery("Case IH Axial-Flow 7150 Combine");
               Machinery nh = machineryRepository.findByNameMachinery("New Holland CR8.90 Combine");

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