package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.InningResponseDto;
import com.example.cricketApp.Entity.Inning;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InningService {

    ResponseEntity<List<InningResponseDto>> getAllInnings();

    void createInning(int matchId, Inning inning, int overs, int tId1, int tId2);
}