package com.example.cricketApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "overs")
public class Over {
    @Id
    private ObjectId overId;
    private int overNumber;
    private int playerBowling;
    private int matchId;
    private ObjectId inningId;

    public Over(int overNumber, int playerBowling, int matchId, ObjectId inningId) {
        this.overNumber = overNumber;
        this.playerBowling = playerBowling;
        this.matchId = matchId;
        this.inningId = inningId;
    }
}
