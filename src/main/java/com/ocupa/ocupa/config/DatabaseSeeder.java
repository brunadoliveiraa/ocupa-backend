package com.ocupa.ocupa.config;

import com.ocupa.ocupa.service.SeedingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final SeedingService seedingService;

    @Override
    public void run(String... args) throws Exception {
        seedingService.seedDatabase();
    }
}
