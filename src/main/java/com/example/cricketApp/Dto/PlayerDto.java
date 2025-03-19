package com.example.cricketApp.Dto;


import com.example.cricketApp.Entity.PlayerType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDto {
    private int playerId;
    private String playerName;
    private PlayerType playerType;
}
