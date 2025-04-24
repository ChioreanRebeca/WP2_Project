package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Long> {
}
