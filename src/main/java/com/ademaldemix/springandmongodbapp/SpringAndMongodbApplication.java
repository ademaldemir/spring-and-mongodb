package com.ademaldemix.springandmongodbapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringAndMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAndMongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository) {
        return args -> {
            Address address = new Address("Turkey", "Istanbul","64000");
            String email = "ademaldemir@gmail.com";
            Student student = new Student(
                    "Adem",
                    "Aldemir",
                    email,
                    Gender.MALE,
                    address,
                    Arrays.asList("Computer Science", "Data Science"),
                    BigDecimal.TEN,
                    LocalDateTime.now());

            // usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);

            Optional<Student> studentByEmail = repository.findStudentByEmail(email);
            if (studentByEmail.isPresent()) {
                throw new IllegalStateException("Found many students with email " + email);
            } else {
                System.out.println("Inserting student " + student);
                repository.insert(student);
            }
        };
    }

    private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1){
            throw new IllegalStateException("Found many students with email " + email);
        }

        if (students.isEmpty()) {
            System.out.println("Inserting student " + student);
            repository.insert(student);
        } else {
            System.out.println(student + " already exists.");
        }
    }

}
