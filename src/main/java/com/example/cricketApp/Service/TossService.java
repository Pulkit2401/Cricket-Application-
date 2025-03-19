package com.example.cricketApp.Service;
import com.example.cricketApp.Dto.MatchDto;
import org.springframework.http.ResponseEntity;

public interface TossService {

    ResponseEntity<?> performToss(MatchDto matchDto, int teamId1, int teamId2);


}
