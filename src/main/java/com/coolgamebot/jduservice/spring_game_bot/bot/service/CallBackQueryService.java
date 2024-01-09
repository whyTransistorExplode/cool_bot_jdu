package com.coolgamebot.jduservice.spring_game_bot.bot.service;

import com.coolgamebot.jduservice.spring_game_bot.api_service.ShortGameService;
import com.coolgamebot.jduservice.spring_game_bot.bot.game.train_word_game.TrainWordGameEngine;
import com.coolgamebot.jduservice.spring_game_bot.service.CategoryService;
import com.coolgamebot.jduservice.spring_game_bot.service.UtilityService;
import com.coolgamebot.jduservice.spring_game_bot.session_management.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.coolgamebot.jduservice.spring_game_bot.bot.Constants.*;

@Service
@RequiredArgsConstructor
public class CallBackQueryService {
//    private final ShortGameService shortGameService;
//    private final SessionService sessionService;
    private final TrainWordGameEngine twge;
//    private final UtilityService utilityService;
//    private final CategoryService categoryService;

    /**
     * callback from keyboards that are attached to message
     *
     * @param update query parameter to be processed by
     * @return message to send data back to client
     */
    public BotApiMethod<?> handleKeyboardMessage(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        final String callbackData = callbackQuery.getData();

//        if (!twge.lookUpGame(update.getCallbackQuery().getFrom().getId()))
////            return twge.sendStartMessages(update);


        try {
            if (callbackData.startsWith(NEXT_BUTTON)) {
                twge.handleNext(update);
            } else if (callbackData.startsWith(PLAY_BUTTON)) {
                twge.handlePlay(update);
            } else if (callbackData.endsWith(LANGUAGE_POSTFIX + TWGE_SUFFIX)) {
                twge.handleChosenLanguage(update);
            } else if (callbackData.startsWith(WORD_CAT_PREFIX)) {
                twge.handleCategoryButtons(update);
            } else if (callbackData.startsWith(CONFIRM_BUTTON)) {
                twge.startGame(update);
            } else if (callbackData.startsWith(BACK_BUTTON)) {
                twge.handleBack(update);
            }
        }catch (Exception e){
            e.printStackTrace();
        };

        return AnswerCallbackQuery.builder()
//                .text("adsfadf")
                .callbackQueryId(callbackQuery.getId())
                .build();
    }

    /**
     * messages that are sent in inline mode like without sending messages to chats, will be handled here
     *
     * @param update
     * @return
     */
    public BotApiMethod<?> handleInlineModeKeyboardMessage(Update update) {
        return null;
    }

    /**
     * callback when any chosen game play button pressed, will be handled its endpoint here
     *
     * @param update
     * @return returns processed message back to end client
     */
    public BotApiMethod<?> handleGameShortName(Update update) {

//        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
//
//        Optional<ShortGame> gameByName = shortGameService.getGameByName(update.getCallbackQuery().getGameShortName());
//
////        update.getCallbackQuery().getMessage().
//
//        if (gameByName.isEmpty()) return answerCallbackQuery;
//        CallbackQuery callbackQuery = update.getCallbackQuery();
//        UserSession expectedSession;
//        expectedSession = sessionService.getNonExpiredSessionByUsername(update.getCallbackQuery().getFrom().getUserName());
//        if (expectedSession != null) {
//            expectedSession.access();
//        } else { //session null
//            sessionService.createSession(callbackQuery.)
//            expectedSession.setUser(User.makeUserFromTGUser(callbackQuery.getFrom()));
//            sessionService.getSessionList().put( callbackQuery.getFrom().getId(), expectedSession);
//        }
//
//        ShortGame shortGame = gameByName.get();
//        answerCallbackQuery.setUrl(shortGame.getGameUrl() + "?sesId=" + expectedSession.getIdInternal()); //+ "?sesId=" + expectedSession.getIdInternal()
//        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
//        return answerCallbackQuery;
        return null;
    }
}
