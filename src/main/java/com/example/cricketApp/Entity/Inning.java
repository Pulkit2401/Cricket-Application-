package com.example.cricketApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("innings")
@Scope("prototype")
@Component
public class Inning {

    @Id
    private ObjectId inningId;
    private int inningNumber;
    private int matchId;
    private int teamBattingId;
    private int teamBowlingId;
    private int totalRuns;
    private int totalWicketsFallen;
    private int totalOvers;
    private int oversBowled;
}
