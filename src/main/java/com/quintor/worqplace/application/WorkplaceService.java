package com.quintor.worqplace.application;

import com.quintor.worqplace.application.dto.WorkplaceDTO;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.data.WorkplaceRepository;
import com.quintor.worqplace.domain.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class WorkplaceService {
    private final ReservationRepository reservationRepository;
    private final WorkplaceRepository workplaceRepository;

    public List<WorkplaceDTO> getAllWorkplaces() {
        return workplaceRepository.findAll().stream().map(WorkplaceDTO::new).collect(Collectors.toList());
    }

    public WorkplaceDTO getWorkplaceById(Long id) {
        return new WorkplaceDTO(workplaceRepository.findById(id).orElseThrow(
                () -> new WorkplaceNotFoundException(id)));
    }

    /*
    public WorkplaceDTO getWorkplaceAvailability(LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> reservations = reservationRepository.findAllByWorkplaceIsNotNull();
    }
     */
}