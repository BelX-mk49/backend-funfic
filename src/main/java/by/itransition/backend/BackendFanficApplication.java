package by.itransition.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BackendFanficApplication {
    public static void main(String[] args) {
        SpringApplication.run(by.itransition.backend.BackendFanficApplication.class, args);
    }
}
