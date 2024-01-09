package com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller;

import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.sup.ControllerEntity;
import com.coolgamebot.jduservice.spring_game_bot.bot.service.MessageService;
import com.coolgamebot.jduservice.spring_game_bot.entity.User;
import com.coolgamebot.jduservice.spring_game_bot.enums.Role;
import com.coolgamebot.jduservice.spring_game_bot.service.UserService;
import com.coolgamebot.jduservice.spring_game_bot.service.UtilityService;
import com.coolgamebot.jduservice.spring_game_bot.session_management.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.coolgamebot.jduservice.spring_game_bot.bot.Constants.*;

@Component
@RequiredArgsConstructor
public class MessageBotController extends ControllerEntity {

    private final MessageService messageService;
    private final UtilityService utilityService;
    private final UserService userService;
    private final SessionService sessionService;

    @Override
    public BotApiMethod<?> onUpdate(Update update) {
        String result = "Sorry! I couldn't understand.";
        if (update.getMessage().isCommand()) {
            final String command = update.getMessage().getText();

           User user =  sessionService.getSession(update.getMessage().getFrom().getId()).getUser();//userService.getUser(utilityService.userFromUpdate(update.getMessage().getFrom(), update.getMessage().getChatId()));

            if (command.startsWith(START)) {
                return messageService.START_Command(update);
            }

            if (utilityService.checkUserRoleInGivenUser(user, Role.ADMIN, Role.SUPER_ADMIN, Role.OWNER)) {
                if (command.startsWith(SAVE_EN)) {
                    result = messageService.saveWordEn(command);
                } else if (command.startsWith(SAVE_UZ)) {
                    result = messageService.saveWordUz(command);
                } else if (command.startsWith(SAVE_JP)) {
                    result = messageService.saveWordJp(command);
                } else if (command.startsWith(SAVE_CATEGORY)) {
                    result = messageService.saveCategory(command);
                } else if (command.startsWith(SAVE_DICT)) {
                    result = messageService.saveDictionary(command);
                } else if (command.startsWith(SHOW_DICTIONARY)) {
                    result = messageService.showDictionary(update);
                }
            }
        }
//        update.getMessage().
        return SendMessage.builder()
                .text(result)
                .chatId(update.getMessage().getChatId())
                .build();
    }


}
