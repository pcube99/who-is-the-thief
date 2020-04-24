package com.project.game.controller;

import com.project.game.models.Room;
import com.project.game.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 7:16 PM
 */

@RestController
public class RoomController {
    private RoomService roomService ;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/test")
    public List<Room> test(){
        return roomService.test();
    }
}
