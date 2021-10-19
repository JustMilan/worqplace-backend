package com.quintor.worqplace.data;

import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByWorkplaceIsInAndWorkplaceIsNotNullAndDate(List<Workplace> workplaces, LocalDate date);
}