package com.coolgamebot.jduservice.spring_game_bot.repository;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordUZ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordUZRepository extends JpaRepository<WordUZ, Long> {
    Optional<WordUZ> findByIdea(String idea);

}
