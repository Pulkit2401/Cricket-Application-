package com.example.cricketApp.Dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamResponseDto {
    private int teamId;
    @NonNull
    private String teamName;
    private int numberOfWins;
}
