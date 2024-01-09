package com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller;

import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.sup.ControllerEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultGame;

import java.util.ArrayList;
import java.util.List;


@Service
public class InlineQueryController extends ControllerEntity {


    public BotApiMethod<?> onUpdate(Update update){
//        System.out.println(update.getInlineQuery().getQuery());

        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();


        answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());

        InlineQueryResultGame inlineQueryResultGame = new InlineQueryResultGame();
        inlineQueryResultGame.setGameShortName("pickfast");
        inlineQueryResultGame.setId("jdu_game_challenger");
        List<InlineQueryResult> inlineQueryResultGames = new ArrayList<>();
        inlineQueryResultGames.add(inlineQueryResultGame);
        answerInlineQuery.setResults(inlineQueryResultGames);

        return answerInlineQuery;
    }
}
