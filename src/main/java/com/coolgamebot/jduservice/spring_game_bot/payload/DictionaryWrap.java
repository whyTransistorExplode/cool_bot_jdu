package com.coolgamebot.jduservice.spring_game_bot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DictionaryWrap {
    Long id;
    String[] word_en;
    String[] word_uz;
    String[] word_jp;
    String[] word_cat;
}
