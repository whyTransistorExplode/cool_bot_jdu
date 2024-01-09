package com.coolgamebot.jduservice.spring_game_bot.api_service;

import com.coolgamebot.jduservice.spring_game_bot.entity.ShortGame;
import com.coolgamebot.jduservice.spring_game_bot.repository.ShortGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortGameService {
    private final ShortGameRepository shortGameRepository;

    public void addGame(ShortGame shortGame) {
        shortGameRepository.save(shortGame);
    }

    public Optional<ShortGame> getGameByName(String shortName){
        return shortGameRepository.findByShortName(shortName);
    }
}
