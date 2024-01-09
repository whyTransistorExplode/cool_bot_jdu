package com.coolgamebot.jduservice.spring_game_bot.repository;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordEN;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordENRepository extends JpaRepository<WordEN, Long> {
    Optional<WordEN> findByIdea(String idea);
}
