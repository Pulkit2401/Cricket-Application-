package com.example.cricketApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InningResponseDto {
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
