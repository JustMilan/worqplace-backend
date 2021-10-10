package com.quintor.worqplace.data;

import com.quintor.worqplace.domain.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

}
