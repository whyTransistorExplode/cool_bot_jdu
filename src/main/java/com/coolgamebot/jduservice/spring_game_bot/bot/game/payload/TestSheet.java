package com.coolgamebot.jduservice.spring_game_bot.bot.game.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestSheet {
    private String question;
    private String answer;
    private String[] optionList;
}
