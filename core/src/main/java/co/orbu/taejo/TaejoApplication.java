package co.orbu.taejo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaejoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaejoApplication.class, args);
    }

}
