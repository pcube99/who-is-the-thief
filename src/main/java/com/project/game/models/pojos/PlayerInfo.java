package com.project.game.models.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 1:09 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInfo {
    @JsonProperty("player_name")
    private String name ;
    @JsonProperty("score")
    private Integer score ;
    @JsonProperty("player_id")
    private String playerId ;
    @JsonProperty("profile_pic")
    @SerializedName("profile_pic")
    private String profilePic;
}
