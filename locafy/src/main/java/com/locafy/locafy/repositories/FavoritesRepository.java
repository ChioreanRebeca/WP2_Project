package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Favorites;
import org.springframework.data.repository.CrudRepository;

public interface FavoritesRepository extends CrudRepository<Favorites, Long> {
}
