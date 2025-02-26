package com.example.cricketApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "player_match_performance")
public class PlayerMatchWiseStats {

    @Id
    private String id;

    private int matchId;
    private int playerId;
    private int runsScored;
    private int sixes;
    private int fours;
    private int wicketsTaken;
    private int ballsFaced;
    private int ballsBowled;
    private int runsGiven;

    public PlayerMatchWiseStats(int matchId, int playerId) {
        this.id = matchId + "_" + playerId;
        this.matchId = matchId;
        this.playerId = playerId;
    }
}
