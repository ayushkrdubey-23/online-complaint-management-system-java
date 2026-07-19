package demo;

import demo.model.Role;
import demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ComplaintApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplaintApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedData(UserService userService) {
        return args -> {
            // Seed a standard demo system administrator and default test customer account on initialization
            userService.createInitialUser("System Admin", "admin@example.com", "admin", Role.ADMIN);
            userService.createInitialUser("Normal Student", "user@example.com", "user", Role.USER);
            System.out.println(">>> Core System Data Seeded: admin@example.com (admin) & user@example.com (user) <<<");
        };
    }
}
