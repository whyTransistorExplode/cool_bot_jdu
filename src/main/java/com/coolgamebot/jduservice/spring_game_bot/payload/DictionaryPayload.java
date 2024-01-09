package com.coolgamebot.jduservice.spring_game_bot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DictionaryPayload {
    List<DictionaryWrap> dictionaryWraps;
}
