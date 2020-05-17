package com.project.game.models.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "round_model")
public class RoundModel {
    @Id
    private String id;
    @JsonProperty("room_code")
    private String roomCode;
    @JsonProperty("rounds")
    private List<RoundInfo> roundInfo;
}
