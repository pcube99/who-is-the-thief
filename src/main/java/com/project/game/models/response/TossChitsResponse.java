package com.project.game.models.response;

import com.project.game.models.pojos.PlayerRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossChitsResponse {
    private Boolean success;
    private Integer roundNo;
    private List<PlayerRole> playerRoles ;
}
