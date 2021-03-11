package ru.volkov.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ErrorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErrorApplication.class, args);
    }

}
