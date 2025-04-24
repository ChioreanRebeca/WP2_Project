package com.locafy.locafy;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.BusinessOwner;
import com.locafy.locafy.domain.Favorites;
import com.locafy.locafy.domain.Local;
import com.locafy.locafy.repositories.BusinessOwnerRepository;
import com.locafy.locafy.repositories.BusinessRepository;
import com.locafy.locafy.repositories.FavoritesRepository;
import com.locafy.locafy.repositories.LocalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LocafyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocafyApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner (LocalRepository localRepository, BusinessRepository businessRepository, FavoritesRepository favoritesRepository,BusinessOwnerRepository businessOwnerRepository) {
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


			//business owner logic

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


			//business logic

			BusinessOwner owner = new BusinessOwner();
			owner.setUsername("ceoAnna");
			owner.setFisrtName("Anna");
			owner.setLastName("Ionescu");
			owner.setEmail("anna@startup.ro");
			owner.setPhoneNumber("+40 700 111 222");
			owner.setPassword("safePass123");
			owner.setAddress("Cluj, str. Observatorului, nr. 15");

			BusinessOwner savedOwner = businessOwnerRepository.save(owner);

			Business biz1 = new Business();
			biz1.setBusinessName("Anna’s Coffee");
			biz1.setPhoneNumber("+40 711 123 456");
			biz1.setEmail("coffee@anna.ro");
			biz1.setWebsite("www.annascoffee.ro");
			biz1.setOwner(savedOwner);

			Business biz2 = new Business();
			biz2.setBusinessName("Anna’s Bakery");
			biz2.setPhoneNumber("+40 711 999 888");
			biz2.setEmail("bakery@anna.ro");
			biz2.setWebsite("www.annasbakery.ro");
			biz2.setOwner(savedOwner);

			businessRepository.saveAll(List.of(biz1, biz2));



			//favorites logic

			List<Local> locals = localRepository.findAll();
			List<Business> businesses = businessRepository.findAll();

			//George Bush and Anna's Coffee
			System.out.println("Locals: " + locals.get(0).getFirstName() + " " + locals.get(0).getLastName());
			System.out.println("Businesses: " + businesses.get(0).getBusinessName());

			if (!locals.isEmpty() && !businesses.isEmpty()) {
				// Create a few favorites (each local adds a business to favorites)
				Favorites favorite1 = new Favorites();
				favorite1.setLocalUser(locals.get(0));
				favorite1.setBusiness(businesses.get(0));



				favoritesRepository.save(favorite1);
			}

		};
	}

}
