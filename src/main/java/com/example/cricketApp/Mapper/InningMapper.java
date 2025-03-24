package com.example.cricketApp.Mapper;

import com.example.cricketApp.Dto.InningResponseDto;
import com.example.cricketApp.Entity.Inning;

public class InningMapper {
    public static InningResponseDto toDto(Inning inning) {
        if (inning == null) {
            return null;
        }
        return InningResponseDto.builder()
                .inningId(inning.getInningId())
                .inningNumber(inning.getInningNumber())
                .matchId(inning.getMatchId())
                .teamBattingId(inning.getTeamBattingId())
                .teamBowlingId(inning.getTeamBowlingId())
                .totalRuns(inning.getTotalRuns())
                .totalOvers(inning.getTotalOvers())
                .totalWicketsFallen(inning.getTotalWicketsFallen())
                .oversBowled(inning.getOversBowled())
                .build();
    }
}
