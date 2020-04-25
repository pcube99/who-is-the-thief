package com.project.game.Dao.room;

import com.project.game.constants.Constants;
import com.project.game.models.PlayerInfo;
import com.project.game.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public ResponseEntity<Room> createRoom(String roomName, Integer noOfRounds) throws Exception {
        log.info("[createRoom] : Request received to create room with roomName: {} and noOfRounds : {}", roomName, noOfRounds);
        String roomCode = UUID.randomUUID().toString().substring(0, 4);
        List<PlayerInfo> players = new ArrayList<>();
        Room room = Room.builder().roomName(roomName).roomCode(roomCode).noOfRounds(noOfRounds).playersInfo(players).build();
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.save(room);
            log.info("[createRoom] Room saved in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[createRoom] mongodb save operation failed");
            throw new Exception("[createRoom] : mongodb save operation failed");
        }
        return new ResponseEntity<>(roomInDb, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Room> findRoom(String roomCode) throws Exception {
        log.info("[findRoom] Request received for roomCode: {}", roomCode);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.findOne(query, Room.class);
            log.info("[findRoom] Room found in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[findRoom] mongodb search operation failed");
            throw new Exception("[findRoom] mongodb search operation failed");
        }
        if (roomInDb == null) {
            log.error("[findRoom] No room found with code: {}", roomCode);
            return new ResponseEntity<>(new Room(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roomInDb, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> joinRoom(String roomCode, String playerName) throws Exception {
        log.info("[joinRoom] Request received to join room: {} from playerName: {}",roomCode,playerName) ;
        ResponseEntity<Room> roomResponseEntity = findRoom(roomCode);
        Room room = roomResponseEntity.getBody();
        if (room.getRoomCode().length() == 0) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
        if (room.getPlayersInfo().size() < 4) {
            String playerId = UUID.randomUUID().toString().substring(0,4) ;
            PlayerInfo newPlayer = PlayerInfo.builder().name(playerName).score(0).playerId(playerId).build();
            room.getPlayersInfo().add(newPlayer);
            Room roomInDb;
            try {
                roomInDb = mongoTemplate.save(room);
                log.info("[joinRoom] Room updated in DB: {}", roomInDb);
            } catch (Exception e) {
                log.error("[joinRoom] mongodb update operation failed");
                throw new Exception("[joinRoom] mongodb update operation failed");
            }
            return new ResponseEntity<>(playerId, HttpStatus.OK);
        }
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<PlayerInfo>> updatePoints(String roomCode, Integer points, String playerId) throws Exception {
        log.info("[updatePoints] Request received to update points for playerId: {} , points: {}, roomCode: {}",playerId,points,roomCode) ;
        ResponseEntity<Room> roomResponseEntity = findRoom(roomCode);
        Room room = roomResponseEntity.getBody();
        List<PlayerInfo> list = room.getPlayersInfo();
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
            log.error("[updatePoints] mongodb update operation failed");
            throw new Exception("[updatePoints] mongodb update operation failed");
        }
        return new ResponseEntity<>(list,HttpStatus.OK) ;
    }

}
