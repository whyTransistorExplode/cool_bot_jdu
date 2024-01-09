package com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.sup;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class ControllerEntity {

    public abstract BotApiMethod<?> onUpdate(Update update);
}
