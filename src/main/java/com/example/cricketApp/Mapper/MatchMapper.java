package com.example.cricketApp.Mapper;

import com.example.cricketApp.Dto.MatchResponseDto;
import com.example.cricketApp.Entity.Match;

public class MatchMapper {
    public static MatchResponseDto toDto(Match match) {
        if (match == null) {
            return null;
        }
        return MatchResponseDto.builder()
                .matchId(match.getMatchId())
                .teamWonId(match.getTeamWonId())
                .teamAId(match.getTeamAId())
                .teamBId(match.getTeamBId())
                .venue(match.getVenue())
                .overs(match.getOvers())
                .matchDate(match.getMatchDate())
                .build();
    }
}
