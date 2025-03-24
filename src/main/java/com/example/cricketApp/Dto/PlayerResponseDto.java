package com.example.cricketApp.Dto;


import com.example.cricketApp.Entity.PlayerType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerResponseDto {
    private int playerId;
    private int teamId;
    @NonNull
    private String playerName;
    private PlayerType playerType;
    private int matchesPlayed;
    private int runsScored;
    private int wicketsTaken;
    private int ballsPlayed;
    private int fifties;
    private int hundreds;
    private int fiveWicketHauls;
}
