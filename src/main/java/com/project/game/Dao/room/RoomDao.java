package com.project.game.Dao.room;

import com.project.game.models.Room;
import org.springframework.http.ResponseEntity;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 11:47 PM
 */
public interface RoomDao {
    ResponseEntity<Room> findRoom(String roomCode) throws Exception;

    ResponseEntity<Room> createRoom(String roomName, Integer noOfRounds, String firstPlayerName) throws Exception;

    ResponseEntity<Boolean> joinRoom(String roomCode, String playerName) throws Exception;

}
