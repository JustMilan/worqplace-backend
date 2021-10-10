package com.quintor.worqplace.application;

import com.quintor.worqplace.application.dto.WorkplaceAvailibilityDTO;
import com.quintor.worqplace.application.dto.WorkplaceDTO;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import com.quintor.worqplace.data.WorkplaceRepository;
import com.quintor.worqplace.domain.Workplace;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;

    public List<WorkplaceAvailibilityDTO> getAllWorkplaces() {
        return workplaceRepository.findAll().stream().map(WorkplaceAvailibilityDTO::new).collect(Collectors.toList());
    }

    public WorkplaceAvailibilityDTO getWorkplaceAvailibilityDTOById(Long id) {
        Workplace workplace = workplaceRepository.findById(id).orElseThrow(
                () -> new WorkplaceNotFoundException("Workplace " + id + " not found"));
        return new WorkplaceAvailibilityDTO(workplace);
    }

    public Workplace getWorkplaceById(Long id) {
        return workplaceRepository.findById(id).orElseThrow(
                () -> new WorkplaceNotFoundException("Workplace " + id + " not found"));
    }

    public void saveWorkplace(Workplace workplace) {
        workplaceRepository.save(workplace);
    }

    public List<WorkplaceDTO> getAllAvailableWorkplaces() {
        var allWorkplaces = workplaceRepository.findAll();
        List<Workplace> available = new ArrayList<>();

        allWorkplaces.forEach(workplace -> {
            workplace.getTimeslots().stream().filter(timeslot -> timeslot.getReservation() == null).map(timeslot -> workplace).forEach(available::add);
        });

        return available.stream().map(WorkplaceDTO::new).collect(Collectors.toList());
    }
}