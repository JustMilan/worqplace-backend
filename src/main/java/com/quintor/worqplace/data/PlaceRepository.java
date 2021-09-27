package com.quintor.worqplace.data;

import com.quintor.worqplace.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findPlaceById(long id);
}
