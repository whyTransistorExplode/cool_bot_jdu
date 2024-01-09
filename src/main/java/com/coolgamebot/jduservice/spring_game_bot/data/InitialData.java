package com.coolgamebot.jduservice.spring_game_bot.data;

import com.coolgamebot.jduservice.spring_game_bot.api_service.ShortGameService;
import com.coolgamebot.jduservice.spring_game_bot.entity.ShortGame;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.*;
import com.coolgamebot.jduservice.spring_game_bot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class InitialData {

    private final ShortGameService shortGameService;
    private final CategoryRepository categoryRepository;
    private final WordENRepository wordENRepository;
    private final WordJPRepository wordJPRepository;
    private final WordUZRepository wordUZRepository;
    private final DictionaryRepository dictionaryRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    //    @EventListener(ApplicationReadyEvent.class)
    public void runAfterSetup() {
        if (!ddl.startsWith("create")) return;

        ShortGame shortGame = ShortGame.builder()
                .shortName("pickfast")
                .shortGameId("game_0")
                .gameUrl("http://127.0.0.1:5500/public_html/") //  https://challenge-yourself-with-us.000webhostapp.com/
                .build();
        shortGameService.addGame(shortGame);

//        category("words");
//        category("phrases");
//        category("vegetables");
        Category fruits = category("Fruits");

        WordUZ olma = wordUZ("olma", fruits);
        WordEN wordEN = wordEN("apple", fruits);
        WordJP jpApple = wordJP("りんご", fruits);

        dictionary(olma, wordEN, jpApple);

        // short game initial data's
        addShortGame("pickfast", "game_1", "https://challenge-yourself-with-us.000webhostapp.com/");
//        addShortGame("train", "game_1", "https://challenge-yourself-with-us.000webhostapp.com/");
        addShortGame("admin_panel", "game_1", "https://challenge-yourself-with-us.000webhostapp.com/");
    }

    public void addShortGame(String shortName, String gameId, String gameUrl) {
        shortGameService.addGame(ShortGame.builder()
                .shortGameId(gameId)
                .shortName(shortName)
                .gameUrl(gameUrl)
                .build());
    }

    public Category category(String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        return categoryRepository.save(category);
    }

    public WordEN wordEN(String name, Category category) {
        WordEN wordEN = WordEN.builder()
                .idea(name)
                .categories(new HashSet<>(Collections.singleton(category)))
                .build();
        return wordENRepository.save(wordEN);
    }

    public WordUZ wordUZ(String name, Category category) {
        WordUZ wordUZ = WordUZ.builder()
                .idea(name)
                .categories(new HashSet<>(Collections.singleton(category)))
                .build();
        return wordUZRepository.save(wordUZ);
    }

    public WordJP wordJP(String name, Category category) {
        WordJP wordJP = WordJP.builder()
                .idea(name)
                .categories(new HashSet<>(Collections.singleton(category)))
                .build();
        return wordJPRepository.save(wordJP);
    }

    public void dictionary(WordUZ wuz, WordEN wen, WordJP wjp) {
        Dictionary dictionary = Dictionary.builder()
                .wordEN(Collections.singleton(wen))
                .wordJP(Collections.singleton(wjp))
                .wordUZ(Collections.singleton(wuz))
                .build();
        dictionaryRepository.save(dictionary);
    }
}
