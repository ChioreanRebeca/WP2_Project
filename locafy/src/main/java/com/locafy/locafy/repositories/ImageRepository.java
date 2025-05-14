package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
