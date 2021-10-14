package com.quintor.worqplace.application;

import com.quintor.worqplace.application.dto.ReservationDTO;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.domain.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new ReservationNotFoundException(id));
        return new ReservationDTO(reservation);
    }

    public List<ReservationDTO> getReservationByWorkplacesNotNull() {
        return reservationRepository.findAllByWorkplaceIsNotNull().stream().map(ReservationDTO::new).collect(Collectors.toList());
    }
}