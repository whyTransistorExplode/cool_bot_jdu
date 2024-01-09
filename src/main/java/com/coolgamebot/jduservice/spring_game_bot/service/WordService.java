package com.coolgamebot.jduservice.spring_game_bot.service;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordEN;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordJP;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordUZ;
import com.coolgamebot.jduservice.spring_game_bot.repository.WordENRepository;
import com.coolgamebot.jduservice.spring_game_bot.repository.WordJPRepository;
import com.coolgamebot.jduservice.spring_game_bot.repository.WordUZRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class WordService {
    private final WordENRepository wordENRepository;
    private final WordUZRepository wordUZRepository;
    private final WordJPRepository wordJPRepository;

    public WordEN addWordEn(WordEN wordEN) {
        if (wordEN.getIdea() == null ||wordEN.getIdea().length() < 1 || wordEN.getIdea().length() > 100)
            return null;
        return wordENRepository.findByIdea(wordEN.getIdea()).orElseGet(() -> wordENRepository.save(wordEN));
    }

    public WordUZ addWordUz(WordUZ wordUZ) {
        if (wordUZ.getIdea() == null ||wordUZ.getIdea().length() < 1 || wordUZ.getIdea().length() > 100)
            return null;
        return wordUZRepository.findByIdea(wordUZ.getIdea()).orElseGet(() -> wordUZRepository.save(wordUZ));
    }

    public WordJP addWordJp(WordJP wordJP) {
        if (wordJP.getIdea() == null ||wordJP.getIdea().length() < 1 || wordJP.getIdea().length() > 100)
            return null;
        return wordJPRepository.findByIdea(wordJP.getIdea()).orElseGet(() -> wordJPRepository.save(wordJP));
    }

    public List<WordEN> getWordsEn() {
        return wordENRepository.findAll();
    }

    public List<WordUZ> getWordsUz() {
        return wordUZRepository.findAll();

    }

    public List<WordJP> getWordsJp() {
        return wordJPRepository.findAll();
    }
}
