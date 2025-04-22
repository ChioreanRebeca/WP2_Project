package com.locafy.locafy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LocafyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocafyApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner (LocalRepository repository) {
		return args -> {
			Local local1 = new Local(
					"george123",
					"george123@gmail.com",
					"123abc",
					"George",
					"Bush",
					"+40 723 213 5432",
					"Bcharest Sector6, street, number 123"
			);

			Local local2 = new Local(
					"oana",
					"oanamaria@gmail.com",
					"4oana32432",
					"Oana",
					"Maria",
					"+40 723 543 1343",
					"Pitesti, street, nr. 234"
			);



			repository.save(local1);
			repository.save(local2);

		};
	}
}
