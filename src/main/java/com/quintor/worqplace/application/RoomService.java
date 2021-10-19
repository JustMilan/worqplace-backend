package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.RoomNotFoundException;
import com.quintor.worqplace.data.RoomRepository;
import com.quintor.worqplace.domain.Room;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new RoomNotFoundException(id));
    }
}
