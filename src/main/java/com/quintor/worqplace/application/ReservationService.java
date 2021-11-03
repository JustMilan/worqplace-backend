package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import com.quintor.worqplace.application.exceptions.RoomNotAvailableException;
import com.quintor.worqplace.application.exceptions.WorkplacesNotAvailableException;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.domain.Employee;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ReservationService {
	private final EmployeeService employeeService;
	private final RoomService roomService;
	private final ReservationRepository reservationRepository;

	public List<Reservation> getAllReservations() {
		return reservationRepository.findAll();
	}

	public Reservation getReservationById(Long id) {
		return reservationRepository
				.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
	}

	public Reservation reserveWorkplaces(ReservationDTO reservationDTO) {
		Reservation reservation = this.toReservation(reservationDTO);
		Room room = reservation.getRoom();
		int available = room.getCapacity() - room.countReservedWorkspaces(reservation.getDate(),
				reservation.getStartTime(), reservation.getEndTime());
		int wanted = reservation.getWorkplaceAmount();

		if (available >= wanted) {
			room.addReservation(reservation);
			reservationRepository.save(reservation);
			return reservation;
		} else throw new WorkplacesNotAvailableException(wanted, available);
	}

	public Reservation toReservation(ReservationDTO reservationDTO) {
		Employee employee = employeeService.getEmployeeById(reservationDTO.getEmployeeId());
		Room room = roomService.getRoomById(reservationDTO.getRoomId());
		System.out.println();

		return new Reservation(reservationDTO.getDate(), reservationDTO.getStartTime(), reservationDTO.getEndTime(),
				employee, room, reservationDTO.getWorkplaceAmount(), reservationDTO.getRecurrence());
	}

	/**
	 * @param reservationDTO DTO input for creating a reservation.
	 * @return {@link List<Reservation>} of all the reserved workplaces
	 */
	public Reservation reserveRoom(ReservationDTO reservationDTO) {
		Reservation reservation = toReservation(reservationDTO);
		reservation.setWorkplaceAmount(reservation.getRoom().getCapacity());

		boolean available = roomService.isRoomAvailable(reservation.getRoom(), reservationDTO.getDate(), reservationDTO.getStartTime(), reservationDTO.getEndTime());
		if (!available) throw new RoomNotAvailableException();

		return reservationRepository.save(reservation);
	}

	public List<Reservation> getAllMyReservations(Long id){
		return reservationRepository.findAllByEmployeeId(id);
	}
}
