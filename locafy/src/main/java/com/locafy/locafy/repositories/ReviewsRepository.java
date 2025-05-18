package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Reviews;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewsRepository extends CrudRepository<Reviews, Long> {
    List<Reviews> findByBusinessId(Long businessId);
}
