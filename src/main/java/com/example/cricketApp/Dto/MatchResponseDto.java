package com.example.cricketApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResponseDto {
    private int matchId;
    private int teamWonId;
    private int teamAId;
    private int teamBId;
    private String venue;
    private int overs;
    private LocalDate matchDate;
}
