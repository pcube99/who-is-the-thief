package com.project.game.controller;

import com.project.game.models.Room;
import com.project.game.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 7:16 PM
 */

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
    public ResponseEntity<Room> findRoom(@PathVariable("room_code") String roomCode) throws Exception {
        return roomService.findRoom(roomCode);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestParam("room_name") String roomName, @RequestParam("rounds") Integer noOfRounds) throws Exception {
        return roomService.createRoom(roomName, noOfRounds);
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Boolean> joinRoom(@RequestParam("room_code") String roomCode, @RequestParam("player_name") String playerName) throws Exception {
        return roomService.joinRoom(roomCode, playerName);
    }

}
