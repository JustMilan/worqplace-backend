package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.WorkplaceNotAvailableException;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import com.quintor.worqplace.application.exceptions.InvalidReservationTypeException;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.domain.Employee;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.domain.Workplace;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ReservationService {
    private final EmployeeService employeeService;
    private final WorkplaceService workplaceService;
    private final RoomService roomService;
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(
                () -> new ReservationNotFoundException(id));
    }

    public Reservation reserveWorkplace(ReservationDTO reservationDTO) {
        Reservation reservation = toReservation(reservationDTO);

        List<Workplace> availableWorkplaces = workplaceService.getWorkplacesAvailability(reservation.getWorkplace().getRoom().getLocation().getId(),
                reservation.getDate(), reservation.getStartTime(), reservation.getEndTime());

        if (reservation.getWorkplace() == null)
            throw new InvalidReservationTypeException();

        // check if workplace is available
        if (availableWorkplaces.stream().noneMatch(workplace -> workplace.getId().equals(reservation.getWorkplace().getId())))
            throw new WorkplaceNotAvailableException();

        reservationRepository.save(reservation);

        return reservation;
    }

    public Reservation toReservation(ReservationDTO reservationDTO) {
        Employee employee = employeeService.getEmployeeById(reservationDTO.getEmployeeId());
        Workplace workplace = reservationDTO.getWorkplaceId() != null? workplaceService.getWorkplaceById(reservationDTO.getWorkplaceId()) : null;
        Room room = reservationDTO.getRoomId() != null? roomService.getRoomById(reservationDTO.getRoomId()) : null;

        return new Reservation(reservationDTO.getDate(), reservationDTO.getStartTime(), reservationDTO.getEndTime(), employee, room, workplace, reservationDTO.isRecurring());
    }

}
