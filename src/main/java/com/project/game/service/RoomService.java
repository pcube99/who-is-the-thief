package com.project.game.service;

import com.project.game.constants.Constants;
import com.project.game.models.pojos.PlayerInfo;
import com.project.game.models.pojos.PlayerRole;
import com.project.game.models.pojos.Room;
import com.project.game.models.pojos.RoundInfo;
import com.project.game.models.pojos.RoundModel;
import com.project.game.models.response.TossChitsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 1:14 PM
 */
@Service
@Slf4j
public class RoomService {
    private MongoTemplate mongoTemplate;

    @Autowired
    public RoomService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public Room findRoom(String roomCode) throws Exception {
        log.info("[findRoom] Request received for roomCode: {}", roomCode);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.findOne(query, Room.class);
            log.info("[findRoom] Room found in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[findRoom] mongodb search operation failed, e - {}", e.getMessage());
            throw e;
        }
        if (roomInDb == null) {
            log.error("[findRoom] No room found with code: {}", roomCode);
            return new Room();
        }
        return roomInDb;
    }

    public Room createRoom(String roomName, Integer noOfRounds) throws Exception {
        log.info("[createRoom] : Request received to create room with roomName: {} and noOfRounds : {}", roomName, noOfRounds);
        String roomCode = UUID.randomUUID().toString().substring(0, 4);
        List<PlayerInfo> players = new ArrayList<>();
        Room room = Room.builder().roomName(roomName).roomCode(roomCode).playersInfo(players).build();
        RoundModel roundModel = RoundModel.builder().roomCode(roomCode).roundInfo(Collections.emptyList()).build();
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.save(room);
            mongoTemplate.save(roundModel, "round_model");
            log.info("[createRoom] Room saved in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[createRoom] mongodb save operation failed, e - {}", e.getMessage());
            throw e;
        }
        return roomInDb;
    }

    public String joinRoom(String roomCode, String playerName, String profilePic) throws Exception {
        log.info("[joinRoom] Request received to join room: {} from playerName: {}", roomCode, playerName);
        Room room = findRoom(roomCode);
        if (room.getRoomCode().length() == 0) {
            return "";
        }
        if (room.getPlayersInfo().size() < 4) {
            String playerId = UUID.randomUUID().toString().substring(0, 4);
            PlayerInfo newPlayer = PlayerInfo.builder().name(playerName).score(0).playerId(playerId).isReady(false).profilePic(profilePic).build();
            room.getPlayersInfo().add(newPlayer);
            Room roomInDb;
            try {
                roomInDb = mongoTemplate.save(room);
                log.info("[joinRoom] Room updated in DB: {}", roomInDb);
            } catch (Exception e) {
                log.error("[joinRoom] mongodb update operation failed, e - {}", e.getMessage());
                throw e;
            }
            return playerId;
        }
        return "";
    }

    public List<PlayerInfo> updatePoints(String roomCode, Integer points, String playerId) throws Exception {
        log.info("[updatePoints] Request received to update points for playerId: {} , points: {}, roomCode: {}", playerId, points, roomCode);
        Room room = findRoom(roomCode);
        List<PlayerInfo> list = room.getPlayersInfo();
        if (list == null) {
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
            log.error("[updatePoints] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
        return list;
    }

    public TossChitsResponse checkAllReady(String roomCode, String playerId) throws Exception {
        Room room = findRoom(roomCode);
        if (room == null) {
            return TossChitsResponse.builder().success(false).build();
        }
        List<PlayerInfo> players = room.getPlayersInfo();
        if (players.size() < 4) {
            return TossChitsResponse.builder().success(false).build();
        }
        boolean flag = true;
        int i = 0;
        for (PlayerInfo player : players) {
            if (player.getPlayerId().equals(playerId)) {
                player.setIsReady(true);
                players.set(i, player);
            } else {
                flag = flag & player.getIsReady();
            }
            i++;
        }
        room.setPlayersInfo(players);
        mongoTemplate.save(room);
        log.info("Returning flag : {}", flag);
        if (flag) {
            return tossChits(roomCode);
        }
        return TossChitsResponse.builder().success(false).build();
    }

    public TossChitsResponse tossChits(String roomCode) throws Exception {
        log.info("Request received to toss Cheats");
        List<String> roles = Arrays.asList(Constants.ROLES);
        Collections.shuffle(roles);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        RoundModel roundModel = mongoTemplate.findOne(query, RoundModel.class);
        if (roundModel == null || roundModel.getRoundInfo() == null) {
            return null;
        }
        Room room = findRoom(roomCode);

        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerInfo> playerInfoList = room.getPlayersInfo();
        List<PlayerRole> playerRoles = new ArrayList<>();
        List<String> playerIds = new ArrayList<>();

        for (PlayerInfo playerInfo : playerInfoList) {
            playerIds.add(playerInfo.getName());
        }
        for (int i = 0; i < playerInfoList.size(); i++) {
            PlayerRole playerRole = PlayerRole.builder().playerId(playerIds.get(i)).role(roles.get(i)).build();
            playerRoles.add(playerRole);
        }

        RoundInfo roundInfo = RoundInfo.builder().playerRoleList(playerRoles).build();
        roundInfoList.add(roundInfo);
        roundModel.setRoundInfo(roundInfoList);

        try {
            roundModel = mongoTemplate.save(roundModel, Constants.COLLECTION_ROUND_MODEL);
            log.info("[tossChits] roundModel updated in DB: {}", roundModel);
        } catch (Exception e) {
            log.error("[tossChits] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }

        Integer roundNo = roundModel.getRoundInfo().size();
        resetReadyState(room);

        return TossChitsResponse.builder().roundNo(roundNo).playerRoles(playerRoles).success(true).build();
    }
    public Boolean updateStatus(String roomCode, String playerId) throws Exception {
        Room room = findRoom(roomCode);
        if (room == null || room.getPlayersInfo().size() < 4) {
            return false;
        }
        List<PlayerInfo> players = room.getPlayersInfo();
        for (PlayerInfo player : players) {
            if (player.getPlayerId().equals(playerId))
                player.setIsReady(true);
        }
        room.setPlayersInfo(players);
        try {
            Room roomInDb = mongoTemplate.save(room);
            log.info("[updateStatus] Room updated in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[updateStatus] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
        return true;
    }

    public void resetReadyState(Room room) {
        List<PlayerInfo> players = room.getPlayersInfo();
        int i = 0;
        for (PlayerInfo player : players) {

            player.setIsReady(false);
            players.set(i, player);
            i++;
        }
        room.setPlayersInfo(players);
        try {
            Room roomInDb = mongoTemplate.save(room, Constants.COLLECTION_ROOM_MODEL);
            log.info("[resetReadyState] Room updated in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[resetReadyState] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
    }
}