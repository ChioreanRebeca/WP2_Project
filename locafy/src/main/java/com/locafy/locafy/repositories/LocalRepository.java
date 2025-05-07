package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
A repository in Spring Data JPA is a special interface that lets you interact with the database without writing any SQL or boilerplate code.
* It handles all the CRUD operations (Create, Read, Update, Delete) for your entity.
*
* By extending JpaRepository, Spring will automatically provide methods like:

        Method	                What it does
    save(Local local)	    Save or update an entity
    findById(Long id)   	Fetch by primary key
    findAll()	            Get all rows
    deleteById(Long id)    	Delete by ID
    existsById(Long id)	    Check if a row exists
    count()	                Count rows in the table

* */




public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByUserName(String userName);
    
}
