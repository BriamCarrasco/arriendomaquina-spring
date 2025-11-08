package com.briamcarrasco.arriendomaquinaria.model;

import com.briamcarrasco.arriendomaquinaria.model.User.Role;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import com.briamcarrasco.arriendomaquinaria.repository.UserRepository;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MachineryRepository machineryRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@demo.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user1") == null) {
            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("user1@demo.com");
            user1.setPassword(passwordEncoder.encode("password"));
            user1.setRole(Role.USER);
            userRepository.save(user1);
        }

        if (userRepository.findByUsername("user2") == null) {
            User user2 = new User();
            user2.setUsername("user2");
            user2.setEmail("user2@demo.com");
            user2.setPassword(passwordEncoder.encode("password"));
            user2.setRole(Role.USER);
            userRepository.save(user2);
        }

        if (!machineryRepository.existsByNameMachinery("John Deere 5075E Tractor")) {
            machineryRepository.save(new Machinery(
                    null,
                    "John Deere 5075E Tractor",
                    "Agriculture",
                    "Available",
                    new BigDecimal("150.00")));
        }
        if (!machineryRepository.existsByNameMachinery("Case IH Axial-Flow 7150 Combine")) {
            machineryRepository.save(new Machinery(
                    null,
                    "Case IH Axial-Flow 7150 Combine",
                    "Agriculture",
                    "Available",
                    new BigDecimal("350.00")));
        }
        if (!machineryRepository.existsByNameMachinery("New Holland CR8.90 Combine")) {
            machineryRepository.save(new Machinery(
                    null,
                    "New Holland CR8.90 Combine",
                    "Agriculture",
                    "Available",
                    new BigDecimal("400.00")));
        }
        if (!machineryRepository.existsByNameMachinery("Kubota M7-171 Tractor")) {
            machineryRepository.save(new Machinery(
                    null,
                    "Kubota M7-171 Tractor",
                    "Agriculture",
                    "Available",
                    new BigDecimal("180.00")));
        }
        if (!machineryRepository.existsByNameMachinery("Claas Lexion 780 Combine")) {
            machineryRepository.save(new Machinery(
                    null,
                    "Claas Lexion 780 Combine",
                    "Agriculture",
                    "Available",
                    new BigDecimal("420.00")));
        }
    }
}