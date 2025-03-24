package com.example.cricketApp.Mapper;

import com.example.cricketApp.Dto.TeamResponseDto;
import com.example.cricketApp.Entity.Team;

public class TeamMapper {
    public static TeamResponseDto toDto(Team team) {
        if (team == null) {
            return null;
        }
        return TeamResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .numberOfWins(team.getNumberOfWins())
                .build();
    }
}
