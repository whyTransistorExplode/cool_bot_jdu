package com.coolgamebot.jduservice.spring_game_bot.repository;

import com.coolgamebot.jduservice.spring_game_bot.entity.ShortGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortGameRepository extends JpaRepository<ShortGame, Integer> {
    Optional<ShortGame> findByShortName(String shortName);
}
