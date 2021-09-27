package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.PlaceService;
import com.quintor.worqplace.application.exceptions.PlaceNotFoundException;
import com.quintor.worqplace.presentation.dto.InputPlaceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/place")
@AllArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        return new ResponseEntity<>(placeService.getAllPlaces(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPlaceById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(placeService.getPlaceById(id), HttpStatus.OK);
        } catch (PlaceNotFoundException placeNotFoundException) {
            return new ResponseEntity<>(placeNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPlace(InputPlaceDTO place) {
        return new ResponseEntity<>(placeService.createPlace(place), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePlace(@PathVariable long id, InputPlaceDTO place){
        try {
            return new ResponseEntity<>(placeService.updatePlace(id, place), HttpStatus.OK);
        } catch (PlaceNotFoundException placeNotFoundException) {
            return new ResponseEntity<>(placeNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePlace(@PathVariable long id){
        try {
            placeService.deletePlace(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (PlaceNotFoundException placeNotFoundException) {
            return new ResponseEntity<>(placeNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
