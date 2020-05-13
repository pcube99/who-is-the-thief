package com.project.game.Dao.room;

import com.project.game.constants.Constants;
import com.project.game.models.PlayerInfo;
import com.project.game.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 11:48 PM
 */
@Component
@Slf4j
public class RoomDaoImpl implements RoomDao {
    private MongoTemplate mongoTemplate;

    @Autowired
    public RoomDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Room createRoom(String roomName, Integer noOfRounds) throws Exception {
        log.info("[createRoom] : Request received to create room with roomName: {} and noOfRounds : {}", roomName, noOfRounds);
        String roomCode = UUID.randomUUID().toString().substring(0, 4);
        List<PlayerInfo> players = new ArrayList<>();
        Room room = Room.builder().roomName(roomName).roomCode(roomCode).noOfRounds(noOfRounds).playersInfo(players).build();
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.save(room);
            log.info("[createRoom] Room saved in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[createRoom] mongodb save operation failed, e - {}",e.getMessage());
            throw e;
        }
        return roomInDb;
    }

    @Override
    public Room findRoom(String roomCode) throws Exception {
        log.info("[findRoom] Request received for roomCode: {}", roomCode);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.findOne(query, Room.class);
            log.info("[findRoom] Room found in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[findRoom] mongodb search operation failed, e - {}",e.getMessage());
            throw e;
        }
        if (roomInDb == null) {
            log.error("[findRoom] No room found with code: {}", roomCode);
            return new Room();
        }
        return roomInDb;
    }

    @Override
    public String joinRoom(String roomCode, String playerName, String profilePic) throws Exception {
        log.info("[joinRoom] Request received to join room: {} from playerName: {}",roomCode,playerName) ;
        Room room = findRoom(roomCode);
        if (room.getRoomCode().length() == 0) {
            return "";
        }
        if (room.getPlayersInfo().size() < 4) {
            String playerId = UUID.randomUUID().toString().substring(0,4) ;
            PlayerInfo newPlayer = PlayerInfo.builder().name(playerName).score(0).playerId(playerId).isReady(false).profilePic(profilePic).build();
            room.getPlayersInfo().add(newPlayer);
            Room roomInDb;
            try {
                roomInDb = mongoTemplate.save(room);
                log.info("[joinRoom] Room updated in DB: {}", roomInDb);
            } catch (Exception e) {
                log.error("[joinRoom] mongodb update operation failed, e - {}",e.getMessage());
                throw e;
            }
            return playerId;
        }
        return "";
    }

    @Override
    public List<PlayerInfo> updatePoints(String roomCode, Integer points, String playerId) throws Exception {
        log.info("[updatePoints] Request received to update points for playerId: {} , points: {}, roomCode: {}",playerId,points,roomCode) ;
        Room room = findRoom(roomCode);
        List<PlayerInfo> list = room.getPlayersInfo();
        if(list == null)
        {
            return Collections.EMPTY_LIST;
        }
        for (PlayerInfo playerInfo : list) {
            if (playerInfo.getPlayerId().equals(playerId)) {
                Integer currentScore = playerInfo.getScore();
                playerInfo.setScore(currentScore + points);
                break;
            }
        }
        room.setPlayersInfo(list);
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.save(room);
            log.info("[updatePoints] Room updated in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[updatePoints] mongodb update operation failed, e - {}",e.getMessage());
            throw e;
        }
        return list;
    }

    @Override
    public Boolean checkAllReady(String roomCode, String playerId) throws Exception {

        Room room = findRoom(roomCode) ;
        if(room == null)
        {
            return false;
        }
        List<PlayerInfo> players = room.getPlayersInfo() ;
        if(players.size() < 4)
        {
            return false;
        }
        Boolean flag = true ;
        for(PlayerInfo player: players)
        {
            if(player.getPlayerId().equals(playerId))continue;
            flag = flag & player.getIsReady() ;
        }
        return flag;
    }

    @Override
    public List<String> tossChits() {
        List<String> roles = Arrays.asList("Raja", "Wazir", "Chor", "Sipahi");
        Collections.shuffle(roles);
        return roles;
    }

    @Override
    public Boolean updateStatus(String roomCode, String playerId) throws Exception {
        Room room = findRoom(roomCode) ;
        if(room == null || room.getPlayersInfo().size()<4)
        {
            return false;
        }
        List<PlayerInfo> players = room.getPlayersInfo();
        for(PlayerInfo player: players)
        {
            if(player.getPlayerId().equals(playerId))
            player.setIsReady(true);
        }
        room.setPlayersInfo(players);
        try {
            Room roomInDb = mongoTemplate.save(room);
            log.info("[updateStatus] Room updated in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[updateStatus] mongodb update operation failed, e - {}",e.getMessage());
            throw e;
        }
        return true;
    }

}
