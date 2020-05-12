package com.project.game.Dao.room;

import com.project.game.models.PlayerInfo;
import com.project.game.models.Room;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 11:47 PM
 */
public interface RoomDao {
    ResponseEntity<Room> findRoom(String roomCode) throws Exception;

    ResponseEntity<Room> createRoom(String roomName, Integer noOfRounds) throws Exception;

    ResponseEntity<String> joinRoom(String roomCode, String playerName) throws Exception;

    ResponseEntity<List<PlayerInfo>> updatePoints(String roomCode,Integer points,String playerId) throws Exception ;

    ResponseEntity<Boolean> checkAllReady(String roomCode, String playerId) throws Exception ;
}
