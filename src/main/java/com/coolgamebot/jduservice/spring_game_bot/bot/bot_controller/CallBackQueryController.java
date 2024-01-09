package com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller;

import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.sup.ControllerEntity;
import com.coolgamebot.jduservice.spring_game_bot.bot.service.CallBackQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * this class controls flows of keyboard events, either when pressed in inlineKeyboard or play_button of game
 */
@Service
@RequiredArgsConstructor
public class CallBackQueryController extends ControllerEntity {
    private final CallBackQueryService callBackQueryService;

    public BotApiMethod<?> onUpdate(Update update) {

        if (update.getCallbackQuery().getMessage() != null) {
            return callBackQueryService.handleKeyboardMessage(update);
        } else if (update.getCallbackQuery().getInlineMessageId() != null) {
            return callBackQueryService.handleInlineModeKeyboardMessage(update);
        } else if (update.getCallbackQuery().getGameShortName() != null) {
            return callBackQueryService.handleGameShortName(update);
        }

        return AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
    }
}
