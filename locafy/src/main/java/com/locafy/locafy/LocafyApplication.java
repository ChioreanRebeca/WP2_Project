package com.locafy.locafy;

import com.locafy.locafy.domain.*;
import com.locafy.locafy.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class LocafyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocafyApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner (LocalRepository localRepository, BusinessRepository businessRepository,
										 FavoritesRepository favoritesRepository, BusinessOwnerRepository businessOwnerRepository, ImageRepository imageRepository, AdminRepository adminRepository) {
		return args -> {

			/// locals logic
			Local local1 = new Local(
					"george123",
					"george123@gmail.com",
					"{noop}123abc",
					"George",
					"Bush",
					"+40 723 213 5432",
					"Bcharest Sector6, street, number 123"
			);
			local1.getRoles().add("ROLE_LOCAL");

			Local local2 = new Local(
					"oana",
					"oanamaria@gmail.com",
					"{noop}4oana32432", //{noop} is for testing purposes so that I don't have to provide an encoded passwd
					"Oana",
					"Maria",
					"+40 723 543 1343",
					"Pitesti, street, nr. 234"
			);
			local2.getRoles().add("ROLE_LOCAL");  // Hardcoding the ROLE_LOCAL directly



			localRepository.save(local1);
			localRepository.save(local2);


			///business owner logic
			BusinessOwner owner1 = new BusinessOwner();
			owner1.setUsername("biz123");
			owner1.setPassword("{noop}pass123");
			owner1.setFirstName("Alex");
			owner1.setLastName("Popescu");
			owner1.setEmail("alex@example.com");
			owner1.setPhoneNumber("+40 700 000 000");
			owner1.setAddress("Timisoara, str. Libertatii, nr. 10");

			BusinessOwner owner2 = new BusinessOwner();
			owner2.setUsername("startupguy");
			owner2.setPassword("{noop}securepass");
			owner2.setFirstName("Andrei");
			owner2.setLastName("Nitu");
			owner2.setEmail("andrei@startup.ro");
			owner2.setPhoneNumber("+40 711 222 333");
			owner2.setAddress("Cluj, str. Observatorului, nr. 15");

			BusinessOwner savedOwner1 = businessOwnerRepository.save(owner1);
			businessOwnerRepository.save(owner2);


			///business logic
			BusinessOwner owner = new BusinessOwner();
			owner.setUsername("ceoMihai");
			owner.setFirstName("Mihai");
			owner.setLastName("Ionescu");
			owner.setEmail("mihai@startup.ro");
			owner.setPhoneNumber("+40 700 111 222");
			owner.setPassword("{noop}safePass123");
			owner.setAddress("Cluj, str. Observatorului, nr. 15");

			BusinessOwner savedOwner = businessOwnerRepository.save(owner);

			Business biz1 = new Business();
			biz1.setBusinessName("Mihai’s Coffee");
			biz1.setPhoneNumber("+40 711 123 456");
			biz1.setEmail("coffee@mihai.ro");
			biz1.setWebsite("www.scoffee.ro");
			biz1.setDescription("Coffee");
			biz1.setOwner(savedOwner);

			Business biz2 = new Business();
			biz2.setBusinessName("Mihai’s Bakery");
			biz2.setPhoneNumber("+40 711 999 788");
			biz2.setEmail("bakery@mihai.ro");
			biz2.setWebsite("www.mbakery.ro");
			biz2.setDescription("Bakery");
			biz2.setOwner(savedOwner);

			Business biz3 = new Business();
			biz3.setBusinessName("Mihai’s Cabbage Farm");
			biz3.setPhoneNumber("+40 711 999 777");
			biz3.setEmail("cabbages@mihai.ro");
			biz3.setWebsite("www.mihaiscabbages.ro");
			biz3.setDescription("Good delicious cabbages");
			biz3.setOwner(savedOwner);

			//business from owner1
			Business biz4 = new Business();
			biz4.setBusinessName("Alex's Bakery");
			biz4.setPhoneNumber("+40 711 999 775");
			biz4.setEmail("business@alex.ro");
			biz4.setWebsite("www.alexbusiness.ro");
			biz4.setDescription("Delicious bread and pretzels");
			biz4.setOwner(savedOwner1);

			businessRepository.saveAll(List.of(biz1, biz2, biz3, biz4));


			/// images for business 3
			List<Image> images = new ArrayList<>();

			try {
				String[] fileNames = {"cabbage1.png", "cabbage2.png", "cabbage3.png", "cabbage4.png", "cabbage5.png"};
				for (String fileName : fileNames) {
					ClassPathResource imgFile = new ClassPathResource("static/images/" + fileName);
					byte[] imageData = imgFile.getInputStream().readAllBytes();
					Image image = new Image();
					image.setData(imageData);
					image.setBusiness(biz3);
					images.add(image);
				}
				imageRepository.saveAll(images);
			} catch (IOException e) {
				e.printStackTrace();
			}

			/// image for biz1: Coffee
			try {
				ClassPathResource imgFile = new ClassPathResource("static/images/coffee.png");
				byte[] imageData = imgFile.getInputStream().readAllBytes();

				Image image = new Image();
				image.setData(imageData);
				image.setBusiness(biz1);

				imageRepository.save(image);
			} catch (IOException e) {
				e.printStackTrace();
			}

			/// image for biz2: Bakery
			try {
				ClassPathResource imgFile = new ClassPathResource("static/images/bakery.png");
				byte[] imageData = imgFile.getInputStream().readAllBytes();

				Image image = new Image();
				image.setData(imageData);
				image.setBusiness(biz2);

				imageRepository.save(image);
			} catch (IOException e) {
				e.printStackTrace();
			}

			/// image for biz4 owner1: Alex Bakery
			try {
				ClassPathResource imgFile = new ClassPathResource("static/images/pretzel.png");
				byte[] imageData = imgFile.getInputStream().readAllBytes();

				Image image = new Image();
				image.setData(imageData);
				image.setBusiness(biz4);

				imageRepository.save(image);
			} catch (IOException e) {
				e.printStackTrace();
			}


			//George Bush and Anna's Coffee
			List<Local> locals = localRepository.findAll();
			List<Business> businesses = businessRepository.findAll();
			System.out.println("Locals: " + locals.get(0).getFirstName() + " " + locals.get(0).getLastName());
			System.out.println("Businesses: " + businesses.get(0).getBusinessName());



			///favorites logic
			if (!locals.isEmpty() && !businesses.isEmpty()) {
				Favorites favorite1 = new Favorites();
				favorite1.setLocalUser(locals.get(0));
				favorite1.setBusiness(businesses.get(0));
				favoritesRepository.save(favorite1);
			}

			/*///review logic
			if (!locals.isEmpty() && !businesses.isEmpty()) {
				Reviews review = new Reviews();
				review.setMessage("Great service and friendly staff! Loved Anna's Coffee!");
				review.setStars(4.5);
				review.setLocal(locals.get(0));
				review.setBusiness(businesses.get(0));

				reviewsRepository.save(review);
			}*/

			/// add admin
			Admin admin = new Admin("admin", "{noop}123456");
			adminRepository.save(admin);

		};
	}

}