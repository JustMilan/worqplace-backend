package com.quintor.worqplace.data;

import com.quintor.worqplace.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
