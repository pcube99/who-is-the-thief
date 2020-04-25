package com.project.game.service;

import com.project.game.Dao.room.RoomDaoImpl;
import com.project.game.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 1:14 PM
 */
@Service
@Slf4j
public class RoomService {
    private RoomDaoImpl roomDaoImpl;

    @Autowired
    public RoomService(RoomDaoImpl roomDaoImpl) {
        this.roomDaoImpl = roomDaoImpl;
    }

    public ResponseEntity<Room> findRoom(String roomCode) throws Exception {
        return roomDaoImpl.findRoom(roomCode);
    }

    public ResponseEntity<Room> createRoom(String roomName, Integer noOfRounds) throws Exception {
        return roomDaoImpl.createRoom(roomName, noOfRounds);
    }

    public ResponseEntity<Boolean> joinRoom(String roomCode, String playerName) throws Exception {
        return roomDaoImpl.joinRoom(roomCode, playerName);
    }
}
