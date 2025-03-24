package com.example.cricketApp.Mapper;

import com.example.cricketApp.Dto.PlayerResponseDto;
import com.example.cricketApp.Entity.Player;

public class PlayerMapper {
    public static PlayerResponseDto toDto(Player player) {
        if (player == null) {
            return null;
        }
        return PlayerResponseDto.builder()
                .playerId(player.getPlayerId())
                .teamId(player.getTeamId())
                .playerName(player.getPlayerName())
                .playerType(player.getPlayerType())
                .matchesPlayed(player.getMatchesPlayed())
                .runsScored(player.getRunsScored())
                .wicketsTaken(player.getWicketsTaken())
                .ballsPlayed(player.getBallsPlayed())
                .fifties(player.getFifties())
                .hundreds(player.getHundreds())
                .fiveWicketHauls(player.getFiveWicketHauls())
                .build();
    }
}
