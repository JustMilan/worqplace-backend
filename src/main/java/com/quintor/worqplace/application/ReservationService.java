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

/**
 * Service class that handles communication between the
 * {@link com.quintor.worqplace.presentation.ReservationController controller},
 * the domain and the {@link ReservationRepository repository}.
 *
 * @see Reservation
 * @see com.quintor.worqplace.presentation.ReservationController ReservationController
 * @see ReservationRepository
 * @see Room
 * @see RoomService
 * @see Employee
 * @see EmployeeService
 */
@Service
@Transactional
@AllArgsConstructor
public class ReservationService {
	private final EmployeeService employeeService;
	private final RoomService roomService;
	private final ReservationRepository reservationRepository;

	/**
	 * Function that gets all {@link Reservation reservations} from the database.
	 *
	 * @return a list containing all {@link Reservation reservations}.
	 * @see Reservation
	 * @see ReservationRepository
	 */
	public List<Reservation> getAllReservations() {
		return reservationRepository.findAll();
	}

	/**
	 * Function that gets the requested {@link Reservation} by the entered id.
	 *
	 * @param id the id of the {@link Reservation}.
	 * @return the {@link Reservation} with the corresponding id.
	 * @throws ReservationNotFoundException when no reservation in the database matches
	 *                                      the entered id, this exception is thrown.
	 * @see Reservation
	 * @see ReservationNotFoundException
	 * @see com.quintor.worqplace.presentation.ReservationController ReservationController
	 */
	public Reservation getReservationById(Long id) {
		return reservationRepository
				.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(id));
	}

	/**
	 * Function that reserves workplaces by calculating the reserved workplaces
	 * and seeing if the {@link Room room's} capacaty allows for more
	 * {@link Reservation reservations}.
	 *
	 * @param reservationDTO the input reservation.
	 * @return the created {@link Reservation} object.
	 * @throws WorkplacesNotAvailableException when the requested amount of workplaces
	 *                                         is not available, this exception is thrown.
	 * @see ReservationDTO
	 * @see Reservation
	 * @see Room
	 */
	public Reservation reserveWorkplaces(ReservationDTO reservationDTO) {
		Reservation reservation = this.toReservation(reservationDTO);
		Room room = reservation.getRoom();
		room.addReservation(reservation);
		reservationRepository.save(reservation);
		return reservation;
	}

	/**
	 * Function that translates a {@link ReservationDTO} to a {@link Reservation}.
	 *
	 * @param reservationDTO the input {@link ReservationDTO}
	 * @return the generated {@link Reservation} object based on the input data.
	 * @see ReservationDTO
	 * @see Reservation
	 * @see Employee
	 * @see Room
	 */
	public Reservation toReservation(ReservationDTO reservationDTO) {
		Employee employee = employeeService.getEmployeeById(reservationDTO.getEmployeeId());
		Room room = roomService.getRoomById(reservationDTO.getRoomId());
		int workplaceAmount = Math.max(reservationDTO.getWorkplaceAmount(), 1);

		return new Reservation(reservationDTO.getDate(),
				reservationDTO.getStartTime(), reservationDTO.getEndTime(),
				employee, room, workplaceAmount,
				reservationDTO.getRecurrence());
	}

	/**
	 * Function that reserves a whole {@link Room} by checking if it's available
	 * and saving the {@link Reservation} object if so.
	 *
	 * @param reservationDTO DTO input for creating a reservation.
	 * @return the generated {@link Reservation} object.
	 * @throws RoomNotAvailableException when the selected {@link Room} is not available,
	 *                                   this exception is thrown.
	 * @see ReservationDTO
	 * @see Reservation
	 * @see ReservationRepository
	 * @see RoomService
	 * @see RoomNotAvailableException
	 */
	public Reservation reserveRoom(ReservationDTO reservationDTO) {
		Reservation reservation = this.toReservation(reservationDTO);
		Room room = reservation.getRoom();
		reservation.setWorkplaceAmount(room.getCapacity());
		room.addReservation(reservation);
		return 	reservationRepository.save(reservation);
	}

	/**
	 * Function that gets all {@link Reservation reservations} made by
	 * the entered {@link Employee}.
	 *
	 * @param id id of the wanted {@link Employee}.
	 * @return a list of {@link Reservation reservations} made by
	 * the selected {@link Employee}
	 * @see Employee
	 * @see Reservation
	 * @see ReservationRepository
	 */
	public List<Reservation> getAllMyReservations(Long id) {
		return reservationRepository.findAllByEmployeeId(id);
	}
}
