package com.example.cricketApp.Dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamRequestDto {
    private int teamId;
    @NonNull
    private String teamName;
}
