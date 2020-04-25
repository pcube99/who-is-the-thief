package com.project.game.Dao.room;

import com.project.game.constants.Constants;
import com.project.game.exception.DocumentNotFoundException;
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
    public ResponseEntity<Room> findRoom(String roomCode) throws Exception {
        log.info("Request received for room_code: {}", roomCode);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        Room roomInDb = null;
        try {
            roomInDb = mongoTemplate.findOne(query, Room.class);
            log.info("Room found in DB: {}", roomInDb);
        } catch (Exception e) {
            log.info("mongodb search operation failed");
            throw new Exception("mongodb search operation failed");
        }
        if (roomInDb == null) {
            log.info("No room found with code: {}", roomCode);
            throw new DocumentNotFoundException("No room with roomCode: { " + roomCode + " }");
        }
        return new ResponseEntity<>(roomInDb, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<Room> createRoom(String roomName, Integer noOfRounds, String firstPlayerName) throws Exception {
        log.info("Request received to create room with roomName: [{}] , noOfRounds : [{}] and firstPlayerName : [{}]",roomName,noOfRounds,firstPlayerName) ;
        String roomCode = UUID.randomUUID().toString().substring(0, 4);
        List<PlayerInfo> players = new ArrayList<>();
        players.add(PlayerInfo.builder().name(firstPlayerName).score(0).build());
        Room room = Room.builder().roomName(roomName).roomCode(roomCode).noOfRounds(noOfRounds).playersInfo(players).build();
        Room roomInDb = null;
        try {
            roomInDb = mongoTemplate.save(room);
            log.info("Room saved in DB: {}", roomInDb);
        } catch (Exception e) {
            log.info("mongodb save operation failed");
            throw new Exception("mongodb save operation failed");
        }
        return new ResponseEntity<>(roomInDb, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Boolean> joinRoom(String roomCode, String playerName) throws Exception {
        ResponseEntity<Room> roomResponseEntity = findRoom(roomCode);
        Room room = roomResponseEntity.getBody();
        if (room.getPlayersInfo().size() < 4) {
            PlayerInfo newPlayer = PlayerInfo.builder().name(playerName).score(0).build();
            room.getPlayersInfo().add(newPlayer);
            Room roomInDb = null;
            try {
                roomInDb = mongoTemplate.save(room);
                log.info("Room updated in DB: {}", roomInDb);
            } catch (Exception e) {
                log.info("mongodb update operation failed");
                throw new Exception("mongodb update operation failed");
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
