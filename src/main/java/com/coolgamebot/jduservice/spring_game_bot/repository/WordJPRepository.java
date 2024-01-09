package com.coolgamebot.jduservice.spring_game_bot.repository;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordJP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordJPRepository extends JpaRepository<WordJP, Long> {
    Optional<WordJP> findByIdea(String idea);

}
