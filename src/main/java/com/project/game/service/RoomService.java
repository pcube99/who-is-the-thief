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
        List<PlayerRole> playerRoleList = new ArrayList<>();
        for(int i=0;i<4;i++)
        {
            playerRoleList.add(PlayerRole.builder().role("").playerId("").isReady(false).roundScore(0).build());
        }
        List<RoundInfo> roundInfoList = new ArrayList<>();
        roundInfoList.add(RoundInfo.builder().playerRoleList(playerRoleList).build());
        RoundModel roundModel = RoundModel.builder().roomCode(roomCode).roundInfo(roundInfoList).build();
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.save(room);
            mongoTemplate.save(roundModel, "round_model");
            log.info("[createRoom] Room saved in DB: {}", roomInDb);
            log.info("[createRoom] roundModel saved in DB: {}", roundModel);
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
            PlayerInfo newPlayer = PlayerInfo.builder().name(playerName).score(0).playerId(playerId).profilePic(profilePic).build();
            room.getPlayersInfo().add(newPlayer);
            RoundModel roundModel = findRoundModel(roomCode);
            List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
            List<PlayerRole> playerRoleList = roundInfoList.get(roundInfoList.size()-1).getPlayerRoleList();
            int i=0;
            for(PlayerRole p: playerRoleList)
            {
                if(p.getPlayerId().equals(""))
                {
                    p.setPlayerId(playerId);
                    playerRoleList.set(i,p);
                    break;
                }
                i++;
            }
            roundInfoList.add(RoundInfo.builder().playerRoleList(playerRoleList).build());
            roundModel = RoundModel.builder().roomCode(roomCode).roundInfo(roundInfoList).build();
            Room roomInDb;
            try {
                roomInDb = mongoTemplate.save(room, Constants.COLLECTION_ROOM_MODEL);
                roundModel = mongoTemplate.save(roundModel,Constants.COLLECTION_ROUND_MODEL);
                log.info("[joinRoom] Room updated in DB: {}", roomInDb);
                log.info("[joinRoom] roundModel updated in DB: {}", roundModel);
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

    public Boolean checkAllReady(String roomCode) throws Exception {
       RoundModel roundModel = findRoundModel(roomCode);
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = roundInfoList.get(roundInfoList.size()-1).getPlayerRoleList();
        boolean flag = true;
        for(PlayerRole p: playerRoleList)
        {
            flag = flag && p.getIsReady();
        }
        log.info("Returning flag: {}", flag);
        return flag;
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
            playerIds.add(playerInfo.getPlayerId());
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

        return TossChitsResponse.builder().roundNo(roundNo).playerRoles(playerRoles).success(true).build();
    }

    public Boolean updateStatus(String roomCode, String playerId) throws Exception {
       RoundModel roundModel = findRoundModel(roomCode);
        if (roundModel == null) {
            return false;
        }
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = roundInfoList.get(roundInfoList.size()-1).getPlayerRoleList();
        int i=0;
       for(PlayerRole playerRole: playerRoleList)
       {
           if(playerRole.getPlayerId().equals(playerId))
           {
               playerRole.setIsReady(true);
               playerRoleList.set(i,playerRole);
               break;
           }
           i++;
       }
       roundInfoList.get(roundInfoList.size()-1).setPlayerRoleList(playerRoleList);
       roundModel.setRoundInfo(roundInfoList);
        try {
            RoundModel roundModelInDb = mongoTemplate.save(roundModel);
            log.info("[updateStatus] roundModel updated in DB: {}", roundModel);
        } catch (Exception e) {
            log.error("[updateStatus] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
        return true;
    }

    public Object evaluateScores(String roomCode, String currentPlayerId, String selectedPlayerId) throws Exception {
        Room room = findRoom(roomCode);// to update global score
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        RoundModel roundModel;
        try {
            roundModel = mongoTemplate.findOne(query, RoundModel.class);
        } catch (Exception e) {
            log.error("[evaluateScores] mongodb find operation failed, e - {}", e.getMessage());
            throw e;
        }
        //TODO: null check
        List<RoundInfo> roundInfo = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = roundInfo.get(roundInfo.size() - 1).getPlayerRoleList();
        PlayerRole currentPlayerRole, selectedPlayerRole;
        int currentPlayerIndex = playerRoleList.indexOf(currentPlayerId);
        currentPlayerRole = playerRoleList.get(currentPlayerIndex);
        int selectedPlayerIndex = playerRoleList.indexOf(selectedPlayerId);
        selectedPlayerRole = playerRoleList.get(selectedPlayerIndex);

        Integer chorIndex = getPlayer("Chor", playerRoleList);
        System.out.println(currentPlayerRole.toString() + " " + selectedPlayerRole.toString());
        List<RoundInfo> list = new ArrayList<>();
        switch (currentPlayerRole.getRole()) {
            case "Raja":
     /*           if (takeRisk) {
                    if (selectedPlayerRole.getRole().equals("Chor"))
                        currentPlayerRole.setRoundScore(currentPlayerRole.getRoundScore() + 1200);
                    else {
                        currentPlayerRole.setRoundScore(currentPlayerRole.getRoundScore() + 700);
                        selectedPlayerRole.setRoundScore(selectedPlayerRole.getRoundScore() + 300);
                    }
                } else {
                    if (selectedPlayerRole.getRole().equals("Wazir"))
                        currentPlayerRole.setRoundScore(currentPlayerRole.getRoundScore() + 1000);
                    else {
                        currentPlayerRole.setRoundScore(currentPlayerRole.getRoundScore() + 850);
                        selectedPlayerRole.setRoundScore(selectedPlayerRole.getRoundScore() + 150);
                    }
                }*/
                updateScoreInDb(roundModel, roundInfo, playerRoleList, currentPlayerRole, currentPlayerIndex, 1000);
                break;
            case "Wazir":
                if (selectedPlayerRole.getRole().equals("Chor")) {
                    updateScoreInDb(roundModel, roundInfo, playerRoleList, currentPlayerRole, currentPlayerIndex, 500);
                } else {
                    currentPlayerRole.setRoundScore(0);
                    updateScoreInDb(roundModel, roundInfo, playerRoleList, currentPlayerRole, currentPlayerIndex, 0);
                    updateScoreInDb(roundModel, roundInfo, playerRoleList, playerRoleList.get(chorIndex), currentPlayerIndex, 500);
                }
                break;
            case "Chor":
                updateScoreInDb(roundModel, roundInfo, playerRoleList, currentPlayerRole, currentPlayerIndex, 0);
                break;
            case "Sipahi":
                updateScoreInDb(roundModel, roundInfo, playerRoleList, currentPlayerRole, currentPlayerIndex, 300);
                break;
            default:
                break;
        }
        return "Success";
    }

    private void updateScoreInDb(RoundModel roundModel, List<RoundInfo> roundInfo, List<PlayerRole> playerRoleList, PlayerRole currentPlayerRole, int currentPlayerIndex, int score) {
        List<RoundInfo> list;
        currentPlayerRole.setRoundScore(score);
        playerRoleList.set(currentPlayerIndex, currentPlayerRole);
        roundInfo.get(roundInfo.size() - 1).setPlayerRoleList(playerRoleList);
        list = roundInfo;
        roundModel.setRoundInfo(list);
        mongoTemplate.save(roundModel, "round_model");
    }

    public Integer getPlayer(String role, List<PlayerRole> playerRoleList) {
        Map<Integer, PlayerRole> mp = new HashMap<>();
        for (int k = 0; k < playerRoleList.size(); k++) {
            if (playerRoleList.get(k).getRole().equals(role)) {
                return k;
            }
        }
        return null;
    }

    public RoundModel findRoundModel(String roomCode)
    {
        log.info("[findRoom] Request received for roomCode: {}", roomCode);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        RoundModel roundModel;
        try {
            roundModel = mongoTemplate.findOne(query, RoundModel.class);
            log.info("[findRoom] Room found in DB: {}", roundModel);
        } catch (Exception e) {
            log.error("[findRoom] mongodb search operation failed, e - {}", e.getMessage());
            throw e;
        }
        if (roundModel == null) {
            log.error("[findRoom] No room found with code: {}", roomCode);
            return new RoundModel();
        }
        return roundModel;
    }
}
