package com.project.game.controller;

import com.project.game.models.response.BaseMessageResponse;
import com.project.game.models.pojos.PlayerInfo;
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

import java.util.List;
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
            Room room = roomService.createRoom(roomName, noOfRounds);
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
    @PostMapping("/update-score")
    public BaseMessageResponse updatePoints(@RequestParam("room_code") String roomCode, @RequestParam("points") Integer points, @RequestParam("player_id") String playerId) throws Exception {

        try {
            List<PlayerInfo> playerInfoList = roomService.updatePoints(roomCode, points, playerId);
            return new BaseMessageResponse(playerInfoList);
        } catch (Exception e) {
            log.error("Failed in update-score endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping("/all-ready")
    public BaseMessageResponse checkAllReady(@RequestParam("room_code") String roomCode, @RequestParam("player_id") String playerId) throws Exception {
        try {
            TossChitsResponse response = roomService.checkAllReady(roomCode, playerId);
            return new BaseMessageResponse(response);
        } catch (Exception e) {
            log.error("Failed in all-ready endpoint, e - {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping("/update-status")
    public BaseMessageResponse updateStatus(@RequestParam("room_code") String roomCode, @RequestParam("player_id") String playerId) throws Exception {
        try {
            Boolean flag = roomService.updateStatus(roomCode, playerId);
            return new BaseMessageResponse(flag);
        } catch (Exception e) {
            log.error("Failed in update-status endpoint, e - {}", e.getMessage());
            throw e;
        }
    }
}
