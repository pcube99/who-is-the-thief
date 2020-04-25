package com.project.game.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
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
@Document
public class Room {
    @Id
    private String id ;
    @JsonProperty("room_code")
    @SerializedName("room_code")
    private String roomCode = "" ;
    @JsonProperty("room_name")
    @SerializedName("room_name")
    private String roomName = "";
    @JsonProperty("player_info")
    @SerializedName("player_info")
    private List<PlayerInfo> playersInfo = new ArrayList<>();
    @JsonProperty("no_of_rounds")
    @SerializedName("no_of_rounds")
    private Integer noOfRounds = 0 ;
}


