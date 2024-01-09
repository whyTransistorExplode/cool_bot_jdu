package com.coolgamebot.jduservice.spring_game_bot.bot.game;

import com.coolgamebot.jduservice.spring_game_bot.bot.game.payload.TestSheet;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
//@RequiredArgsConstructor
public class GameSession {
    private final long GAME_TIME_LIMIT = 60000;

    private long user_id;
    private long chat_id;
    private int message_id;
    private boolean isStarted;
    private boolean isEnd;
    private long startTime;
    private final long createdTime = System.currentTimeMillis();

    List<Category> categories;
    List<Integer> selectedCategories;
    Iterator<TestSheet> tests;
    private TestSheet currentTestSheet;
    private int testIndex;
    private Map<TestSheet, Integer> answeredList;
    private String [] chosenLanguages;

    public void start() {
        startTime = System.currentTimeMillis();
        isStarted = true;
        isEnd = false;
        testIndex = 0;
        answeredList = new HashMap<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession that = (GameSession) o;
        return user_id == that.user_id;
    }

    public List<Integer> getSelectedCategories() {
        if (selectedCategories == null)
            selectedCategories = new ArrayList<>();
        return selectedCategories;
    }

    public boolean hasNextTestSheet(){
        return tests.hasNext();
    }
    public TestSheet getNextTestSheet(){
        testIndex++;
        currentTestSheet = tests.next();
        return currentTestSheet;
    }
    public void answerTestSheet(Integer id){
        if (answeredList == null) answeredList = new HashMap<>();
        answeredList.put(currentTestSheet, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }

    public void stop() {
        isEnd = true;
        currentTestSheet = null;
        tests = null;
    }
}
