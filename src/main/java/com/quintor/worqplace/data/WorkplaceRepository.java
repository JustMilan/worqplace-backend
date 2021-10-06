package com.quintor.worqplace.data;

import com.quintor.worqplace.domain.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

}