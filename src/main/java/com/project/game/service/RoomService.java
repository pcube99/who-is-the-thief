package com.project.game.service;

import com.project.game.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 1:14 PM
 */
@Service
public class RoomService {
    private MongoTemplate mongoTemplate;

    @Autowired
    public RoomService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Room> test() {
        return mongoTemplate.findAll(Room.class) ;
    }
}
