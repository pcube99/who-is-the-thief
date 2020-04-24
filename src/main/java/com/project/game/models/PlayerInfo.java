package com.project.game.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("name")
    private String name ;
    @JsonProperty("score")
    private Integer score ;
}
