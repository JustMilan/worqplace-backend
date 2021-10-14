package com.quintor.worqplace.application;

import com.quintor.worqplace.application.dto.ReservationDTO;
import com.quintor.worqplace.application.dto.WorkplaceReservationDTO;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import com.quintor.worqplace.data.EmployeeRepository;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.domain.Employee;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Workplace;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ReservationService {
    private final WorkplaceService workplaceService;
    private final ReservationRepository reservationRepository;
    private final EmployeeRepository employeeRepository;

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new ReservationNotFoundException("Reservation " + id + " not found"));
        return new ReservationDTO(reservation);
    }

    //TODO: Add employee id parameter
    //TODO: Add from and to parameter
    public WorkplaceReservationDTO placeReservationByWorkplaceId(Long id) {
        Workplace workplace = workplaceService.getWorkplaceById(id);
        //TODO: Get id from parameter
        Employee employee = employeeRepository.findById(6L).orElseThrow();

        //TODO: Make this a safe reservation. Now it gets created anyway even if it is already reserved.
        Reservation reservation = new Reservation(1L, employee, workplace.getTimeslots(), workplace);
        workplace.getTimeslots().forEach(timeslot -> timeslot.setReservation(reservation));

        boolean successful;
        var saveWorkplace = workplaceService.saveWorkplace(workplace);

//        Example of what could be done if domain structure has been fixed.
//        for (var timeslot : saveWorkplace.getTimeslots()) {
//            if (timeslot.getStartTime() == from && timeslot.getEndTime() == to && timeslot.getReservation().getEmployee().getId() == employeeId) {
//                succesful = true;
//            } else {
//                succesful = false;
//            }
//        }

//        For development purposes
        successful = true;

        return new WorkplaceReservationDTO(workplace, successful);
    }
}
