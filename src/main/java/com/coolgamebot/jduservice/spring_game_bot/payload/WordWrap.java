package com.coolgamebot.jduservice.spring_game_bot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordWrap {
    private String [] names;
    private String category;
}
