package com.quintor.worqplace.application;

import com.quintor.worqplace.application.dto.PlaceDTO;
import com.quintor.worqplace.application.exceptions.PlaceNotFoundException;
import com.quintor.worqplace.data.PlaceRepository;
import com.quintor.worqplace.domain.Place;
import com.quintor.worqplace.presentation.dto.InputPlaceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
@AllArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public ArrayList<Place> getAllPlaces() {
        return (ArrayList<Place>) placeRepository.findAll();
    }

    public PlaceDTO getPlaceById(Long id) throws PlaceNotFoundException {
        Place place = placeRepository.findPlaceById(id).orElseThrow(
                () -> new PlaceNotFoundException("Place " + id + " not found"));
        return new PlaceDTO(place);
    }

    public PlaceDTO createPlace(InputPlaceDTO place) {
        return new PlaceDTO(placeRepository.save(new Place(place.getName())));
    }

    public PlaceDTO updatePlace(long id, InputPlaceDTO place) throws PlaceNotFoundException {
        Place oldPlace = placeRepository.findPlaceById(id).orElseThrow(
                () -> new PlaceNotFoundException("Place " + id + " not found"));
        oldPlace.setName(place.getName());
        placeRepository.save(oldPlace);
        return new PlaceDTO(oldPlace);
    }

    public void deletePlace(long id) throws PlaceNotFoundException {
        Place place = placeRepository.findPlaceById(id).orElseThrow(
                () -> new PlaceNotFoundException("Place " + id + " not found"));
        placeRepository.delete(place);
    }
}
