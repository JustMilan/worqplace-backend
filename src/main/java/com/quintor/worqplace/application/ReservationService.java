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
		if (availableWorkplaces.stream().noneMatch(workplace -> workplace.getId().equals(reservation.getWorkplace().getId())))
			throw new WorkplaceNotAvailableException();

		reservationRepository.save(reservation);

		return reservation;
	}

	/**
	 * @param reservationDTO DTO input for creating a reservation.
	 * @return {@link List<Reservation>} of all the reserved workplaces
	 */
	public Reservation reserveRoom(ReservationDTO reservationDTO) {
		Reservation reservation = toReservation(reservationDTO);

		if (reservation.getRoom() == null)
			throw new InvalidReservationTypeException();

		boolean available = roomService.isRoomAvailable(reservation.getRoom(), reservationDTO.getDate(), reservationDTO.getStartTime(), reservationDTO.getEndTime());
		if(!available) throw new WorkplaceNotAvailableException();

		reservationRepository.save(reservation);

		return reservation;
	}


	public Reservation toReservation(ReservationDTO reservationDTO) {
        Employee employee = employeeService.getEmployeeById(reservationDTO.getEmployeeId());
        Workplace workplace = reservationDTO.getWorkplaceId() != null? workplaceService.getWorkplaceById(reservationDTO.getWorkplaceId()) : null;
        Room room = reservationDTO.getRoomId() != null? roomService.getRoomById(reservationDTO.getRoomId()) : null;

		return new Reservation(reservationDTO.getDate(), reservationDTO.getStartTime(), reservationDTO.getEndTime(), employee, room, workplace, reservationDTO.isRecurring());
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
								(reservation.getRoom() == null ? Objects.equals(reservation.getWorkplace().getId(), workplaceId)
								:
								reservation.getRoom().getWorkplaces().stream().anyMatch(wp -> Objects.equals(wp.getId(), workplaceId)))
				)
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

		return reservations
				.stream()
				.noneMatch(reservation -> reservation.getStartTime().equals(startTime) ||
						reservation.getEndTime().equals(endTime) ||
						(reservation.getStartTime().isAfter(reservation.getStartTime()) && startTime.isBefore(reservation.getEndTime())) ||
						(endTime.isBefore(reservation.getEndTime()) && endTime.isAfter(reservation.getStartTime())));
	}

	public boolean isWorkplaceAvailableAt(Workplace workplace, LocalDate date) {
		return getReservationsForWorkplaceAtDate(workplace.getId(), date)
				.stream()
				.noneMatch(reservation -> reservation.getDate().equals(date));
	}
}
