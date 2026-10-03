package com.itsupport.app.config;

import com.itsupport.app.model.Ticket;
import com.itsupport.app.model.User;
import com.itsupport.app.repository.TicketRepository;
import com.itsupport.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@itsupport.gr")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("IT Administrator")
                    .department("IT Department")
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("✅ Admin created: admin / admin123");
        }

        // Demo user
        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                    .username("user")
                    .email("user@itsupport.gr")
                    .password(passwordEncoder.encode("user123"))
                    .fullName("Demo User")
                    .department("Sales")
                    .role(User.Role.USER)
                    .build();
            userRepository.save(user);
            log.info("✅ Demo user created: user / user123");
        }

        // Demo tickets
        if (ticketRepository.count() == 0) {
            User user = userRepository.findByUsername("user").orElseThrow();

            Ticket t1 = Ticket.builder()
                    .title("Δεν έχω πρόσβαση στο internet")
                    .description("Από σήμερα το πρωί δεν μπορώ να συνδεθώ στο internet από τον υπολογιστή μου.")
                    .priority(Ticket.Priority.HIGH)
                    .status(Ticket.Status.OPEN)
                    .category(Ticket.Category.NETWORK)
                    .createdBy(user)
                    .build();

            Ticket t2 = Ticket.builder()
                    .title("Ο εκτυπωτής δεν εκτυπώνει")
                    .description("Ο κοινόχρηστος εκτυπωτής του 2ου ορόφου δεν ανταποκρίνεται.")
                    .priority(Ticket.Priority.MEDIUM)
                    .status(Ticket.Status.OPEN)
                    .category(Ticket.Category.HARDWARE)
                    .createdBy(user)
                    .build();

            Ticket t3 = Ticket.builder()
                    .title("Το Microsoft Office δεν ανοίγει")
                    .description("Βλέπω error κατά την εκκίνηση του Word και του Excel.")
                    .priority(Ticket.Priority.LOW)
                    .status(Ticket.Status.RESOLVED)
                    .category(Ticket.Category.SOFTWARE)
                    .createdBy(user)
                    .build();

            ticketRepository.save(t1);
            ticketRepository.save(t2);
            ticketRepository.save(t3);
            log.info("✅ Demo tickets created");
        }
    }
}
