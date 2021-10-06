package com.quintor.worqplace.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timeslot")
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToOne
    private Reservation reservation;

    public boolean hasReservation() {
        if (reservation == null)
            return false;

        if (LocalDate.now().isEqual(date) && LocalTime.now().isAfter(endTime)) {
            return false;
        }

        return !LocalDate.now().isAfter(date);
    }
}
