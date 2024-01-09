package com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller;

import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.sup.ControllerEntity;
import com.coolgamebot.jduservice.spring_game_bot.bot.game.train_word_game.TrainWordGameEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class PollAnswerController extends ControllerEntity {
    private final TrainWordGameEngine twge;

    @Override
    public BotApiMethod<?> onUpdate(Update update) {
//        PollAnswer pollAnswer = update.getPollAnswer();
        return twge.handlePollAnswer(update);
    }
}
