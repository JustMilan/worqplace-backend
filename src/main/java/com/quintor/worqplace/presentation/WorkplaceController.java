package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.WorkplaceService;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("workplaces")
@AllArgsConstructor
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping
    public ResponseEntity<?> getAllWorkplaces() {
        return new ResponseEntity<>(workplaceService.getAllWorkplaces(), HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableWorkplaces() {
        return new ResponseEntity<>(workplaceService.getAllAvailableWorkplaces(), HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkplaceById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(workplaceService.getWorkplaceById(id), HttpStatus.OK);
        } catch (WorkplaceNotFoundException workplaceNotFoundException) {
            return new ResponseEntity<>(workplaceNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @PostMapping("/book/{id}")
    public HttpStatus reserveWorkplace(@PathVariable long id) {
        return HttpStatus.OK;
    }
}