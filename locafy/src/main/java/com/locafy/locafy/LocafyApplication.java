package com.locafy.locafy;

import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.LocalRepository;
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
	CommandLineRunner commandLineRunner (LocalRepository localRepository) {
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



			localRepository.save(local1);
			localRepository.save(local2);


		};
	}

	@Bean
	CommandLineRunner businessOwnerRunner(BusinessOwnerRepository businessOwnerRepository) {
		return args -> {
			BusinessOwner owner1 = new BusinessOwner();
			owner1.setUsername("biz123");
			owner1.setPassword("pass123");
			owner1.setFisrtName("John");
			owner1.setLastName("Doe");
			owner1.setEmail("john@example.com");
			owner1.setPhoneNumber("+40 700 000 000");
			owner1.setAddress("Timisoara, str. Libertatii, nr. 10");

			BusinessOwner owner2 = new BusinessOwner();
			owner2.setUsername("startupgirl");
			owner2.setPassword("securepass");
			owner2.setFisrtName("Ana");
			owner2.setLastName("Popescu");
			owner2.setEmail("ana@startup.ro");
			owner2.setPhoneNumber("+40 711 222 333");
			owner2.setAddress("Cluj, str. Observatorului, nr. 15");

			businessOwnerRepository.save(owner1);
			businessOwnerRepository.save(owner2);
		};
	}

}
