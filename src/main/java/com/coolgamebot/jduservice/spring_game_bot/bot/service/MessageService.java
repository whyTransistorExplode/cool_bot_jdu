package com.coolgamebot.jduservice.spring_game_bot.bot.service;

import com.coolgamebot.jduservice.spring_game_bot.bot.game.train_word_game.TrainWordGameEngine;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Dictionary;
import com.coolgamebot.jduservice.spring_game_bot.payload.DictionaryPayload;
import com.coolgamebot.jduservice.spring_game_bot.payload.DictionaryWrap;
import com.coolgamebot.jduservice.spring_game_bot.payload.WordWrap;
import com.coolgamebot.jduservice.spring_game_bot.service.CategoryService;
import com.coolgamebot.jduservice.spring_game_bot.service.DictionaryService;
import com.coolgamebot.jduservice.spring_game_bot.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static com.coolgamebot.jduservice.spring_game_bot.bot.Constants.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final DictionaryService dictionaryService;
    private final CategoryService categoryService;
    private final TrainWordGameEngine twge;
    private final UtilityService utilityService;

    public String saveWordEn(String data) {
        String[] listWords = data.substring(SAVE_EN.length() + 1).split(" ");
        String category = listWords[0];

        return dictionaryService.addWordsEn(WordWrap.builder()
                .category(category)
                .names(Arrays.copyOfRange(listWords, 1, listWords.length))
                .build()) ? "successful" : "failed";

    }

    public String saveWordUz(String data) {
        String[] listWords = data.substring(SAVE_UZ.length() + 1).split(" ");
        String category = listWords[0];

        return dictionaryService.addWordsUz(WordWrap.builder()
                .category(category)
                .names(Arrays.copyOfRange(listWords, 1, listWords.length))
                .build()) ? "successful" : "failed";
    }

    public String saveWordJp(String data) {
        String[] listWords = data.substring(SAVE_JP.length() + 1).split(" ");
        String category = listWords[0];

        return dictionaryService.addWordsJp(WordWrap.builder()
                .category(category)
                .names(Arrays.copyOfRange(listWords, 1, listWords.length))
                .build()) ? "successful" : "failed";
    }

    public String saveCategory(String data) {
        String[] listWords = data.substring(SAVE_CATEGORY.length() + 1).split(" ");

        for (String listWord : listWords) {
            if (listWord.length() < 1) continue;
            categoryService.addCategory(
                    Category.builder()
                            .name(listWord)
                            .build());
        }
        return "successful";
    }

    public String saveDictionary(String data) {
        String[] listWords = data.substring(SAVE_DICT.length() + 1).split(">");

        if (listWords.length != 4) return "failed: need 1 word_cat and 3 words spaced with '>' to save dictionary";

        dictionaryService.addDictionaries(
                DictionaryPayload.builder()
                        .dictionaryWraps(Collections.singletonList(
                                DictionaryWrap.builder()
                                        .word_cat(new String[]{listWords[0]})
                                        .word_en(new String[]{listWords[1]})
                                        .word_uz(new String[]{listWords[2]})
                                        .word_jp(new String[]{listWords[3]})
                                        .build()
                        ))
                        .build()
        );

        return "successful";
    }

    public String showDictionary(Update update) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Dictionary> dictionaries = dictionaryService.getPreparedTestDictionaries();
        for (int i = 0; i < dictionaries.size(); i++) {
            Dictionary dictionary = dictionaries.get(i);
            stringBuilder.append("d_").append(i + 1)
                    .append(Arrays.toString(dictionary.getWordEN().toArray())).append(" \n")
                    .append(dictionary.getWordUZ()).append(" \n")
                    .append(dictionary.getWordJP()).append(" \n")
                    .append("==========\n");
        }
        utilityService.sendDistributedMessage(stringBuilder.toString(), update.getMessage().getChatId());
        return null;
    }

    public BotApiMethod<?> START_Command(Update update) {
       return twge.handleStart(update);
    }
}
