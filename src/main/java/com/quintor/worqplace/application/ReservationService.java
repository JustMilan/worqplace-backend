package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidReservationTypeException;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import com.quintor.worqplace.application.exceptions.WorkplaceNotAvailableException;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.domain.Employee;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.domain.Workplace;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
		if (! isWorkplaceAvailable(availableWorkplaces, reservation.getWorkplace().getId()))
			throw new WorkplaceNotAvailableException();

		reservationRepository.save(reservation);

		return reservation;
	}

	public static boolean isWorkplaceAvailable(List<Workplace> workplaces, Long workplaceId) {
		return workplaces.stream().anyMatch(workplace -> workplace.getId().equals(workplaceId));
	}

	public Reservation toReservation(ReservationDTO reservationDTO) {
		Employee employee = employeeService.getEmployeeById(reservationDTO.getEmployeeId());
		Workplace workplace = reservationDTO.getWorkplaceId() != null ? workplaceService.getWorkplaceById(reservationDTO.getWorkplaceId()) : null;
		Room room = reservationDTO.getRoomId() != null ? roomService.getRoomById(reservationDTO.getRoomId()) : null;

		return new Reservation(reservationDTO.getDate(), reservationDTO.getStartTime(), reservationDTO.getEndTime(), employee, room, workplace);
	}

	/**
	 * @param workplaceId Long
	 * @param date        LocalDate
	 * @return List of reservations for a workplace at the given date
	 */
	public List<Reservation> getReservationsForWorkplaceAtDate(Long workplaceId, LocalDate date) {
		List<Reservation> reservations = reservationRepository.findAll();

		return reservations
				.stream()
				.filter(reservation ->
						reservation.getDate().toString().equals(date.toString()) &&
								Objects.equals(reservation.getWorkplace().getId(), workplaceId))
				.collect(Collectors.toList());
	}

	public Map<LocalDate, List<Reservation>> getReservationsForWorkplaceAtDates(Long workplaceId, List<LocalDate> dates) {
		Map<LocalDate, List<Reservation>> reservations = new HashMap<>();

		dates.forEach(date -> {
			List<Reservation> reservationsForDate = new ArrayList<>(getReservationsForWorkplaceAtDate(workplaceId, date));
			reservations.put(date, reservationsForDate);
		});

		return reservations;
	}


	public boolean isWorkplaceAvailableAt(Workplace workplace, LocalDate date, LocalTime startTime, LocalTime endTime) {
		List<Reservation> reservations = getReservationsForWorkplaceAtDate(workplace.getId(), date);

		for (Reservation reservation : reservations) {
			LocalTime reservationStartTime = reservation.getStartTime();
			LocalTime reservationEndTime = reservation.getEndTime();
			if (reservationStartTime.equals(startTime) ||
					reservationEndTime.equals(endTime) ||
					(startTime.isAfter(reservationStartTime) && startTime.isBefore(reservationEndTime)) ||
					(endTime.isBefore(reservationEndTime) && endTime.isAfter(reservationStartTime)))
				return false;
		}

		return true;
	}

	public boolean isWorkplaceAvailableAt(Workplace workplace, LocalDate date) {
		return getReservationsForWorkplaceAtDate(workplace.getId(), date)
				.stream()
				.noneMatch(reservation -> reservation.getDate().equals(date));
	}
}
