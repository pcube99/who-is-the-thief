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
import java.util.List;
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

    public Room createRoomAndAllRounds(String roomName, Integer noOfRounds) throws Exception {
        log.info("[createRoom] : Request received to create room with roomName: {} and noOfRounds : {}", roomName, noOfRounds);
        String roomCode = UUID.randomUUID().toString().substring(0, 4);
        List<PlayerInfo> players = new ArrayList<>();
        Room room = Room.builder().roomName(roomName).roomCode(roomCode).playersInfo(players).build();
        Room roomInDb;
        try {
            roomInDb = mongoTemplate.save(room, Constants.COLLECTION_ROOM_MODEL);
            log.info("[createRoom] Room saved in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[createRoom] mongodb save operation failed, e - {}", e.getMessage());
            throw e;
        }
//        createAllRoundsInDb(roomCode, noOfRounds);
        return roomInDb;
    }

    public String joinRoom(String roomCode, String playerName, String profilePic, int noOfRounds) throws Exception {
        log.info("[joinRoom] Request received to join room: {} from playerName: {}", roomCode, playerName);
        Room room = findRoom(roomCode);
        RoundModel roundModel = findRoundModel(roomCode);
        if (room.getRoomCode().length() == 0) {
            return "";
        }
        if (room.getPlayersInfo().size() < 4) {
            String playerId = UUID.randomUUID().toString().substring(0, 4);
            PlayerInfo newPlayer = PlayerInfo.builder().name(playerName).score(0).playerId(playerId).profilePic(profilePic).build();
            room.getPlayersInfo().add(newPlayer);
            Room roomInDb;
            try {
                roomInDb = mongoTemplate.save(room, Constants.COLLECTION_ROOM_MODEL);
                log.info("[joinRoom] Room updated in DB: {}", roomInDb);
            } catch (Exception e) {
                log.error("[joinRoom] mongodb update operation failed, e - {}", e.getMessage());
                throw e;
            }
            if(room.getPlayersInfo().size() == 4)
            {
                log.info("calling createAllRoundsInDb: {}",roomInDb);
                createAllRoundsInDb(roomCode,noOfRounds);
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

    public Boolean checkAllReady(String roomCode, int roundNo) throws Exception {
        RoundModel roundModel = findRoundModel(roomCode);
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = roundInfoList.get(roundNo).getPlayerRoleList();
        boolean flag = true;
        for (PlayerRole p : playerRoleList) {
            flag = flag && p.getIsReady();
        }
        log.info("Returning flag: {}", flag);
        return flag;
    }

    public TossChitsResponse tossChits(String roomCode, int roundNo) throws Exception {
        log.info("Request received to toss Cheats");
        RoundModel roundModel = findRoundModel(roomCode);
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        RoundInfo roundInfo = roundInfoList.get(roundNo);
        return TossChitsResponse.builder().roundNo(roundNo).playerRoles(roundInfo.getPlayerRoleList()).success(true).build();
    }

    private void updateRoundModel(RoundModel roundModel, List<RoundInfo> roundInfoList, List<PlayerRole> playerRoleList) {
        RoundInfo roundInfo = RoundInfo.builder().playerRoleList(playerRoleList).build();
        roundInfoList.set(roundInfoList.size() - 1, roundInfo);
        roundModel.setRoundInfo(roundInfoList);
    }

    private List<PlayerRole> getLatestRoundRoles(List<RoundInfo> roundInfoList) {
        return roundInfoList.get(roundInfoList.size() - 1).getPlayerRoleList();
    }

    public List<String> getShuffledRoles() {
        List<String> roles = Arrays.asList(Constants.ROLES);
        Collections.shuffle(roles);
        return roles;
    }

    public void createRoundModel(RoundModel roundModel) {
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = getLatestRoundRoles(roundInfoList);
        for (PlayerRole p : playerRoleList) {
            if (p.getRole().equals("")) {
                return;
            }
        }
        int index = 0;
        List<PlayerRole> currentRoleList = new ArrayList<>();
        for (PlayerRole playerRole : playerRoleList) {
            currentRoleList.add(getLocalCopyOfPlayerRole(playerRole));
        }
        for (PlayerRole p : currentRoleList) {
            p.setRole("");
            p.setRoundScore(0);
            currentRoleList.set(index, p);
            index++;
        }
        updateRoundModel(roundModel,roundInfoList,currentRoleList);
        try {
            roundModel = mongoTemplate.save(roundModel, Constants.COLLECTION_ROUND_MODEL);
            log.info("[createRoundModel] roundModel updated in DB: {}", roundModel);
        } catch (Exception e) {
            log.error("[createRoundModel] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
    }

    private PlayerRole getLocalCopyOfPlayerRole(PlayerRole playerRole) {
        PlayerRole playerRole1 = new PlayerRole();
        playerRole1.setRole(playerRole.getRole());
        playerRole1.setIsReady(playerRole.getIsReady());
        playerRole1.setRoundScore(playerRole.getRoundScore());
        playerRole1.setPlayerId(playerRole.getPlayerId());
        return playerRole1;
    }

    public Boolean updateStatus(String roomCode, String playerId, int roundNo) throws Exception {
        RoundModel roundModel = findRoundModel(roomCode);
        if (roundModel == null) {
            return false;
        }
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = roundInfoList.get(roundNo).getPlayerRoleList();
        int i = 0;
        for (PlayerRole playerRole : playerRoleList) {
            if (playerRole.getPlayerId().equals(playerId)) {
                playerRole.setIsReady(true);
                playerRoleList.set(i, playerRole);
                break;
            }
            i++;
        }
        roundInfoList.get(roundNo).setPlayerRoleList(playerRoleList);
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

    public Object evaluateScores(String roomCode, String currentPlayerId, String selectedPlayerId, int roundNo) throws Exception {
        RoundModel roundModel = findRoundModel(roomCode);
        List<RoundInfo> roundInfoList = roundModel.getRoundInfo();
        List<PlayerRole> playerRoleList = roundInfoList.get(roundNo).getPlayerRoleList();
        PlayerRole currentPlayerRole, selectedPlayerRole;
        int currentPlayerIndex = 0;
        int selectedPlayerIndex = 0;
        int i=0;
        for(PlayerRole playerRole: playerRoleList)
        {
            if(playerRole.getPlayerId().equals(currentPlayerId)){
                currentPlayerIndex = i;
            }
            if(playerRole.getPlayerId().equals(selectedPlayerId)){
                selectedPlayerIndex = i;
            }
            i++;
        }

        currentPlayerRole = playerRoleList.get(currentPlayerIndex);
        selectedPlayerRole = playerRoleList.get(selectedPlayerIndex);
        Integer chorIndex = getPlayer("Chor", playerRoleList);
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
                updateScoreInDb(roundModel, roundInfoList, playerRoleList, currentPlayerRole, currentPlayerIndex, 1000, roundNo, roomCode);
                break;
            case "Wazir":
                if (selectedPlayerRole.getRole().equals("Chor")) {
                    System.out.println("hre1");
                    updateScoreInDb(roundModel, roundInfoList, playerRoleList, currentPlayerRole, currentPlayerIndex, 500, roundNo, roomCode);
                } else {
                    System.out.println("hre2");
                    PlayerRole localWazir = getLocalCopyOfPlayerRole(currentPlayerRole);
                    localWazir.setRoundScore(0);
                    updateScoreInDb(roundModel, roundInfoList, playerRoleList, localWazir, currentPlayerIndex, 0, roundNo,roomCode );
                    PlayerRole localChor = getLocalCopyOfPlayerRole(playerRoleList.get(chorIndex));
                    updateScoreInDb(roundModel, roundInfoList, playerRoleList, localChor, chorIndex, 500, roundNo, roomCode);
                }
                break;
            case "Chor":
                updateScoreInDb(roundModel, roundInfoList, playerRoleList, currentPlayerRole, currentPlayerIndex, 0, roundNo,roomCode );
                break;
            case "Sipahi":
                updateScoreInDb(roundModel, roundInfoList, playerRoleList, currentPlayerRole, currentPlayerIndex, 300, roundNo, roomCode);
                break;
            default:
                break;
        }
        return "Success";
    }

    private void updateScoreInDb(RoundModel roundModel, List<RoundInfo> roundInfo, List<PlayerRole> playerRoleList, PlayerRole currentPlayerRole, int currentPlayerIndex, int score, int roundNo, String roomCode) throws Exception {
        List<RoundInfo> list;
        currentPlayerRole.setRoundScore(score);
        List<PlayerRole> tempList = new ArrayList<>();
        for (PlayerRole p : playerRoleList) {
            tempList.add(getLocalCopyOfPlayerRole(p));
        }
        tempList.set(currentPlayerIndex, currentPlayerRole);
        roundInfo.get(roundNo).setPlayerRoleList(tempList);
        list = roundInfo;
        roundModel.setRoundInfo(list);
        try {
            RoundModel roundModelInDb = mongoTemplate.save(roundModel, Constants.COLLECTION_ROUND_MODEL);
            log.info("[updateScoreInDb] roundModel updated in DB: {}", roundModelInDb);
        } catch (Exception e) {
            log.error("[updateScoreInDbs] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
        Room room = findRoom(roomCode);
        List<PlayerInfo> playerInfoList = room.getPlayersInfo();
        String currentPlayerId = currentPlayerRole.getPlayerId();
        int i=0;
        for(PlayerInfo p: playerInfoList)
        {
            if(p.getPlayerId().equals(currentPlayerId))
            {
                p.setScore(p.getScore() + score);
                playerInfoList.set(i,p);
                break;
            }
            i++;
        }
        room.setPlayersInfo(playerInfoList);
        try {
            Room roomInDb = mongoTemplate.save(room, Constants.COLLECTION_ROOM_MODEL);
            log.info("[updateScoreInDb] Room updated in DB: {}", roomInDb);
        } catch (Exception e) {
            log.error("[updateScoreInDb] mongodb update operation failed, e - {}", e.getMessage());
            throw e;
        }
    }

    public Integer getPlayer(String role, List<PlayerRole> playerRoleList) {
        for (int k = 0; k < playerRoleList.size(); k++) {
            if (playerRoleList.get(k).getRole().equals(role)) {
                return k;
            }
        }
        return null;
    }

    public RoundModel findRoundModel(String roomCode) {
        log.info("[findRoundModel] Request received for roomCode: {}", roomCode);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ROOM_CODE).is(roomCode));
        RoundModel roundModel;
        try {
            roundModel = mongoTemplate.findOne(query, RoundModel.class);
            log.info("[findRoundModel] Room found in DB: {}", roundModel);
        } catch (Exception e) {
            log.error("[findRoundModel] mongodb search operation failed, e - {}", e.getMessage());
            throw e;
        }
        if (roundModel == null) {
            log.error("[findRoundModel] No room found with code: {}", roomCode);
            return new RoundModel();
        }
        return roundModel;
    }

    public void createAllRoundsInDb(final String roomCode, final int noOfRounds) throws Exception {
        Room room = findRoom(roomCode);
        List<RoundInfo> roundInfoList = new ArrayList<>();
        RoundModel roundModel;
        for(int k=0;k<noOfRounds;k++){
            List<PlayerInfo> playerInfoList = room.getPlayersInfo();
            List<String> roles = getShuffledRoles();
            List<PlayerRole> playerRoleList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                String playerId = playerInfoList.get(i).getPlayerId();
                String role = roles.get(i);
                playerRoleList.add(PlayerRole.builder().role(role).playerId(playerId).isReady(false).roundScore(0).build());
            }
            roundInfoList.add(RoundInfo.builder().playerRoleList(playerRoleList).build());
        }
        try {
            roundModel = RoundModel.builder().roomCode(roomCode).roundInfo(roundInfoList).build();
            mongoTemplate.save(roundModel, Constants.COLLECTION_ROUND_MODEL);
            log.info("[createFirstRoundInDb] roundModel saved in DB: {}", roundModel);
        } catch (Exception e) {
            log.error("[createFirstRoundInDb] mongodb save operation failed, e - {}", e.getMessage());
            throw e;
        }
    }
}
