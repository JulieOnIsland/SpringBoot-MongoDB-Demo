package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(StudentRepository repository,
							 MongoTemplate mongoTemplate) {
		return args -> {
			Address address = new Address(
					"netherland",
					"amsterdam",
					"ne9"
			);
			String email = "camelia@gmail.com";
			Student student = new Student(
					"camelia",
					"lee",
					email,
					Gender.FEMALE,
					address,
					List.of("computer science", "french literature"),
					BigDecimal.TEN,
					LocalDateTime.now()
			);

//			usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);

			repository.findStudentByEmail(email)
					.ifPresentOrElse(s -> {
						System.out.println("already exists");
					}, () -> {
						System.out.println("INSERTING student");
						repository.insert(student);
					});
		};
	}

	private static void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));

		List<Student> students = mongoTemplate.find(query, Student.class);

		if (students.size() > 1) {
			throw new IllegalStateException(
					"found students more than one by this email: "+ email);
		}

		System.out.println("INSERTING student");
		repository.insert(student);
	}
}
