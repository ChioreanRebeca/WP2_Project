package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Reviews;
import org.springframework.data.repository.CrudRepository;

public interface ReviewsRepository extends CrudRepository<Reviews, Long> {
}
