package com.example.cricketApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.JobKOctets;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "balls")
@Component
public class Ball {

    @Id
    private ObjectId ballId;
    private int ballNumber;
    private ObjectId overId;
    private int matchId;
    private ObjectId inningId;
    private int bowlerId;
    private int StrikerId;
    private int NonStrikerId;
    private int runsScored;
    private int TeamScore;
    private int WicketsFallen;
    private boolean isWicket;

    public Ball(int ballNumber, ObjectId overId, int matchId, ObjectId inningId, int bowlerId, int strikerId, int nonStrikerId, int runsScored, int teamScore, int wicketsFallen, boolean isWicket) {
        this.ballNumber = ballNumber;
        this.overId = overId;
        this.matchId = matchId;
        this.inningId = inningId;
        this.bowlerId = bowlerId;
        StrikerId = strikerId;
        NonStrikerId = nonStrikerId;
        this.runsScored = runsScored;
        TeamScore = teamScore;
        WicketsFallen = wicketsFallen;
        this.isWicket = isWicket;
    }
}
