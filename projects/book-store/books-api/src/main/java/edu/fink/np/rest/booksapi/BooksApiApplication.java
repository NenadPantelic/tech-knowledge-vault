package edu.fink.np.rest.booksapi;

import edu.fink.np.rest.booksapi.seed.DataSeeder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BooksApiApplication implements CommandLineRunner {

    private final DataSeeder seeder;

    public BooksApiApplication(DataSeeder seeder) {
        this.seeder = seeder;
    }

    public static void main(String[] args) {
        SpringApplication.run(BooksApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting a seeding runner...");
        seeder.seed();
    }
}
