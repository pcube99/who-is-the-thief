package com.project.game.models.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 1:07 PM
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "room")
public class Room {
    @Id
    private String id ;
    @JsonProperty("room_code")
    private String roomCode = "" ;
    @JsonProperty("room_name")
    private String roomName = "";
    @JsonProperty("player_info")
    private List<PlayerInfo> playersInfo = new ArrayList<>();

}


