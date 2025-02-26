package com.example.cricketApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMatchId implements Serializable {
    private int matchId;
    private int playerId;
}
