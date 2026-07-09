package com.ocupa.ocupa;

import com.ocupa.ocupa.model.User;
import com.ocupa.ocupa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OcupaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(OcupaBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepo) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            
            // Register admin@admin.com
            if (!userRepo.findByEmail("admin@admin.com").isPresent()) {
                User admin = new User();
                admin.setNome("admin");
                admin.setEmail("admin@admin.com");
                admin.setSenha(encoder.encode("admin"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }
            
            // Register admi@admin.com (to cover the typo version)
            if (!userRepo.findByEmail("admi@admin.com").isPresent()) {
                User admin = new User();
                admin.setNome("admin");
                admin.setEmail("admi@admin.com");
                admin.setSenha(encoder.encode("admin"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }
        };
    }
}
