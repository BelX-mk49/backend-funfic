package by.itransition.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableAsync
@SpringBootApplication
public class BackendFanficApplication {
    public static void main(String[] args) {
        SpringApplication.run(by.itransition.backend.BackendFanficApplication.class, args);
    }
}


