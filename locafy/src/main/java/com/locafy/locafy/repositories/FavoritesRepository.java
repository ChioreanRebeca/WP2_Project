package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Favorites;
import com.locafy.locafy.domain.Local;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoritesRepository extends CrudRepository<Favorites, Long> {
    List<Favorites> findByLocalUser(Local localUser);
}
