package com.example.cricketApp.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @Id
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



