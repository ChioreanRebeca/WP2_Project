package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.Favorites;
import com.locafy.locafy.domain.Local;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends CrudRepository<Favorites, Long> {
    List<Favorites> findByLocalUser(Local localUser);
    Optional<Favorites> findByLocalUserAndBusiness(Local localUser, Business business);
}
