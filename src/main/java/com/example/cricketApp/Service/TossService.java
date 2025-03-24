package com.example.cricketApp.Service;
import com.example.cricketApp.Dto.MatchRequestDto;
import org.springframework.http.ResponseEntity;

public interface TossService {

    ResponseEntity<?> performToss(MatchRequestDto matchRequestDto, int teamId1, int teamId2);


}
