package com.project.game.service;

import com.project.game.Dao.room.RoomDaoImpl;
import com.project.game.models.PlayerInfo;
import com.project.game.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Room findRoom(String roomCode) throws Exception {
        return roomDaoImpl.findRoom(roomCode);
    }

    public Room createRoom(String roomName, Integer noOfRounds) throws Exception {
        return roomDaoImpl.createRoom(roomName, noOfRounds);
    }

    public String joinRoom(String roomCode, String playerName, String profilePic) throws Exception {
        return roomDaoImpl.joinRoom(roomCode, playerName, profilePic);
    }

    public List<PlayerInfo> updatePoints(String roomCode, Integer points, String playerId) throws Exception {
        return roomDaoImpl.updatePoints(roomCode, points, playerId);
    }

    public Boolean checkAllReady(String roomCode, String playerId) throws Exception {
        return roomDaoImpl.checkAllReady(roomCode, playerId);
    }

    public List<String> tossChits() {
        return roomDaoImpl.tossChits();
    }

    public Boolean updateStatus(String roomCode, String playerId) throws Exception {
        return roomDaoImpl.updateStatus(roomCode, playerId);
    }
}
