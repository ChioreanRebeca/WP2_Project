package com.locafy.locafy.repositories;

import com.locafy.locafy.domain.Business;
import com.locafy.locafy.domain.Favorites;
import com.locafy.locafy.domain.Local;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends CrudRepository<Favorites, Long> {
    List<Favorites> findByLocalUser(Local localUser);
    Optional<Favorites> findByLocalUserAndBusiness(Local localUser, Business business);

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorites f WHERE f.business.id IN :businessIds")
    void deleteAllByBusinessIds(@Param("businessIds") List<Long> businessIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorites f WHERE f.localUser.id = :localId")
    void deleteAllByLocalId(@Param("localId") Long localId);
}
