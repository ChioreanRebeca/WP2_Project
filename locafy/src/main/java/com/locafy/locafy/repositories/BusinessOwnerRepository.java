package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.BusinessOwner;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BusinessOwnerRepository extends CrudRepository<BusinessOwner, Long> {
    Optional<BusinessOwner> findByUsername(String username);

}
