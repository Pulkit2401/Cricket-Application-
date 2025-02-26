package com.example.cricketApp.Entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "teams")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    private int teamId;
    @NonNull
    private String teamName;
    private int numberOfWins;
}
