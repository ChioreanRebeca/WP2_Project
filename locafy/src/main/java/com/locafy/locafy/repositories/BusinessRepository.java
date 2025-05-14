package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByBusinessName(String businessName);
}
