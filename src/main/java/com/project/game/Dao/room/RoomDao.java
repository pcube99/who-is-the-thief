package com.project.game.Dao.room;

import com.project.game.models.PlayerInfo;
import com.project.game.models.Room;

import java.util.List;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 11:47 PM
 */
public interface RoomDao {
    Room findRoom(String roomCode) throws Exception;

    Room createRoom(String roomName, Integer noOfRounds) throws Exception;

    String joinRoom(String roomCode, String playerName, String profilePic) throws Exception;

    List<PlayerInfo> updatePoints(String roomCode, Integer points, String playerId) throws Exception;

    Boolean checkAllReady(String roomCode, String playerId) throws Exception;

    List<String> tossChits() ;

    Boolean updateStatus(String roomCode, String playerId) throws Exception;
}
