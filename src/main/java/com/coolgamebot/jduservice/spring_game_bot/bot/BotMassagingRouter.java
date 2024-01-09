package com.coolgamebot.jduservice.spring_game_bot.bot;

import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.CallBackQueryController;
import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.InlineQueryController;
import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.MessageBotController;
import com.coolgamebot.jduservice.spring_game_bot.bot.bot_controller.PollAnswerController;
import com.coolgamebot.jduservice.spring_game_bot.service.UserService;
import com.coolgamebot.jduservice.spring_game_bot.service.UtilityService;
import com.coolgamebot.jduservice.spring_game_bot.session_management.SessionService;
import com.coolgamebot.jduservice.spring_game_bot.session_management.entities.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class BotMassagingRouter {
    private final CallBackQueryController callBackQueryController;
    private final InlineQueryController inlineQueryController;
    private final MessageBotController messageBotController;
    private final PollAnswerController pollAnswerController;
    private final SessionService sessionService;
    private final UserService userService;
    private final UtilityService utilityService;

    public BotApiMethod<?> route(Update update) {

//        sessionService.getSession(update)

        if (update.getInlineQuery() != null) {
            sessionUser(update.getInlineQuery().getFrom(), -1);
            return (inlineQueryController.onUpdate(update));

        } else if (update.getCallbackQuery() != null) { //update
            sessionUser(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getMessage().getChatId());
            return (callBackQueryController.onUpdate(update));

        } else if (update.getMessage() != null) {
            sessionUser(update.getMessage().getFrom(), update.getMessage().getChatId());
            return messageBotController.onUpdate(update);

        } else if (update.getPollAnswer() != null) {
            sessionUser(update.getPollAnswer().getUser(), -1);
            return pollAnswerController.onUpdate(update);
        }

        return SendMessage.builder().chatId(update.getMessage().getChat().getId())
                .text("Sorry! we don't have any reaction for this feature yet!")
                .build();
    }

    public void sessionUser(User user, long chat_id) {
        final long user_id = user.getId();
        if (sessionService.getSession(user_id) == null) {
            com.coolgamebot.jduservice.spring_game_bot.entity.User dbUser = userService.getUser(
                    utilityService.userFromUpdate(user, chat_id));
            UserSession session = sessionService.createSession(dbUser.getId());
            session.setUser(dbUser);
            session.setActive(true);
//            sessionService.
        }
    }

}
