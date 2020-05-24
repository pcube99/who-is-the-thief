package com.project.game.controller;

import com.project.game.models.response.BaseMessageResponse;
import com.project.game.models.pojos.Room;
import com.project.game.models.response.TossChitsResponse;
import com.project.game.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 7:16 PM
 */
@Slf4j
@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @CrossOrigin
    @GetMapping("/{room_code}")
    public BaseMessageResponse findRoom(@PathVariable("room_code") String roomCode) throws Exception {
        try {
            Room room = roomService.findRoom(roomCode);
            return new BaseMessageResponse(room);
        } catch (Exception e) {
            log.error("Failed in find room endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping
    public BaseMessageResponse createRoom(@RequestParam("room_name") String roomName, @RequestParam("rounds") Integer noOfRounds) throws Exception {
        try {
            Room room = roomService.createRoomAndAllRounds(roomName, noOfRounds);
            return new BaseMessageResponse(room);
        } catch (Exception e) {
            log.error("Failed in create room endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping
    public BaseMessageResponse joinRoom(@RequestParam("room_code") String roomCode, @RequestParam("player_name") String playerName, @RequestParam("profile_pic") String profilePic) throws Exception {
        try {
            String playerId = roomService.joinRoom(roomCode, playerName, profilePic);
            return new BaseMessageResponse(playerId);
        } catch (Exception e) {
            log.error("Failed in join-room endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping("/all-ready")
    public BaseMessageResponse checkAllReady(@RequestParam("room_code") String roomCode, @RequestParam("round_no") int roundNo) throws Exception {
        try {
            Boolean response = roomService.checkAllReady(roomCode, roundNo);
            Map<String, String> mp = new HashMap<>();
            if(response)
            mp.put("response","true");
            else
                mp.put("response","false");
            return new BaseMessageResponse(response);
        } catch (Exception e) {
            log.error("Failed in all-ready endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping("/update-status")
    public BaseMessageResponse updateStatus(@RequestParam("room_code") String roomCode, @RequestParam("player_id") String playerId, @RequestParam("round_no") int roundNo) throws Exception {
        try {
            Boolean response = roomService.updateStatus(roomCode, playerId, roundNo);
            Map<String, String> mp = new HashMap<>();
            if(response)
                mp.put("response","true");
            else
                mp.put("response","false");
            return new BaseMessageResponse(response);
        } catch (Exception e) {
            log.error("Failed in update-status endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping("/toss-chits")
    public BaseMessageResponse tossChits(@RequestParam("room_code") String roomCode, @RequestParam("round_no") int roundNo) throws Exception {
        try {
            TossChitsResponse flag = roomService.tossChits(roomCode,roundNo);
            return new BaseMessageResponse(flag);
        } catch (Exception e) {
            log.error("Failed in toss-chits endpoint, e - {}", e.getMessage());
            throw e;
        }
    }


    @CrossOrigin
    @PostMapping("/evaluate-scores")
    public Object evaluateScores(@RequestParam("room_code") String roomCode, @RequestParam("player_id") String currentPlayerId,@RequestParam("round_no") int roundNo,
                                 @RequestParam("choice") String selectedPlayerId) throws Exception {
        return roomService.evaluateScores(roomCode, currentPlayerId, selectedPlayerId, roundNo);
    }
}
