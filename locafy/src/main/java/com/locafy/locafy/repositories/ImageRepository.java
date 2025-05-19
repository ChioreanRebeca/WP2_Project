package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBusinessId(Long businessId);
}
