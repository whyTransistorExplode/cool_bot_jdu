package com.coolgamebot.jduservice.spring_game_bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String shortName;

    private String shortGameId;

    private String gameUrl;
}
