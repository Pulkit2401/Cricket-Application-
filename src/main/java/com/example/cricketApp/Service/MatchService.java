package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.MatchRequestDto;
import com.example.cricketApp.Dto.MatchResponseDto;
import com.example.cricketApp.Entity.Match;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {

    ResponseEntity<List<MatchResponseDto>> getAllMatches();

    ResponseEntity<?> playMatch(int matchId);
}
