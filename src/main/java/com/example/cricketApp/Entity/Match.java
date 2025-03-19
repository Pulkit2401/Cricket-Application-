package com.example.cricketApp.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "matches")
@Builder
public class Match {
    @Id
    private int matchId;
    private int teamWonId;
    private int teamAId;
    private int teamBId;
    private String venue;
    private int overs;
    private LocalDate matchDate;
}
