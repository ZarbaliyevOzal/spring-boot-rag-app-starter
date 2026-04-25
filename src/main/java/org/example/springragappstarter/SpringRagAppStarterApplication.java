package org.example.springragappstarter;

import org.example.springragappstarter.service.RagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringRagAppStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRagAppStarterApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(RagService ragService) {
        return args -> {
            String document = "Spring Boot is an open-source Java-based framework used to create microservices,"
                + " web applications, and enterprise applications. It is built on top of the Spring Framework" 
                + " and provides a simplified and opinionated approach to application development. Spring Boot"
                + " eliminates the need for complex configuration and boilerplate code, allowing developers to "
                + "focus on writing business logic. It includes features such as embedded servers, " 
                + "auto-configuration, and production-ready metrics, making it easier to build and deploy "
                + "applications quickly.";
            
            System.out.println("Ingesting document: " + document);
            ragService.ingest(document);
            System.out.println("Document ingested successfully.");

            String question = "What is Spring Boot?";

            System.out.println("Asking question: " + question);
            String answer = ragService.ask(question);
            
            System.out.println("Answer: " + answer);
        };
    }

}
