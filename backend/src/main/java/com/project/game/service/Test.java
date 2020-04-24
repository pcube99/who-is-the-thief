package com.project.game.service;

import com.project.game.models.PlayerInfo;
import com.project.game.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 1:14 PM
 */
@RestController
public class Test {
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/test")
    public Room test() {
        List<PlayerInfo> playerInfoList = new ArrayList<>();
        PlayerInfo playerInfo = PlayerInfo.builder().name("Havya").score(100).build();
        playerInfoList.add(playerInfo);
        Room room = Room.builder().roomCode("CODE").roomName("ROOM_NAME").playersInfo(playerInfoList).noOfRounds(5).build();
        room = mongoTemplate.save(room, "Room");
        System.out.println("ppppppppppp: " + room.toString());
        return room;
    }
}
