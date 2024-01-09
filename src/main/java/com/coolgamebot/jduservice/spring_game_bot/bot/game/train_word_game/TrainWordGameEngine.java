package com.coolgamebot.jduservice.spring_game_bot.bot.game.train_word_game;

import com.coolgamebot.jduservice.spring_game_bot.bot.game.GameSession;
import com.coolgamebot.jduservice.spring_game_bot.bot.game.payload.TestSheet;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.WordEN;
import com.coolgamebot.jduservice.spring_game_bot.service.CategoryService;
import com.coolgamebot.jduservice.spring_game_bot.service.DictionaryService;
import com.coolgamebot.jduservice.spring_game_bot.service.UtilityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.coolgamebot.jduservice.spring_game_bot.bot.Constants.*;

@Component
@RequiredArgsConstructor
public class TrainWordGameEngine {
    private final Map<Long, GameSession> gameSessions;
    private final DictionaryService dictionaryService;
    private final CategoryService categoryService;
    private final UtilityService utilityService;

    /**
     * this method coming either from message service or callback service, so treat it accordingly
     *
     * @param update
     * @return
     */
    public BotApiMethod<?> handleStart(Update update) {
        final long user_id = update.getMessage().getFrom().getId();

        GameSession game = getGame(user_id);
        if (game == null || !game.isStarted() || game.isEnd() ||
                ((System.currentTimeMillis() - game.getStartTime()) > GAME_SESSION_TIME_LIMIT)
        ) {
//            if (game != null) {
//                deleteMessage(game.getMessage_id(), game.getChat_id());
//            }
            //this user exists in game:
            // should be asked whether or not if user wants to leave its active game:
//            final GameSession theGame = getGame(user_id);
//            if (theGame.)
            return sendStartMessages(update);
        }
        return null;
    }

    private void deleteMessage(int message_id, long chat_id) {
        utilityService.sendMessage(
                DeleteMessage.builder()
                        .messageId(message_id)
                        .chatId(chat_id)
                        .build()
        );
    }

    public void handlePlay(Update update) {
        final long user_id = update.getCallbackQuery().getFrom().getId();

        createGame(user_id, update.getCallbackQuery().getMessage().getChatId());
        GameSession game = getGame(user_id);
        game.setCategories(categoryService.getAllCategories());

        Message answer_back = (Message) utilityService.sendMessage(sendLanguages(update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getMessage().getMessageId()));

        game.setMessage_id(answer_back.getMessageId());
    }

    /**
     * handles when user presses next button while game is running
     *
     * @param update
     */
    public void handleNext(Update update) {
        GameSession gameSession = gameSessions.get(update.getCallbackQuery().getFrom().getId());
        if (gameSession == null)
            return;
        utilityService.sendMessage(DeleteMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build());

        if (gameSession.getAnsweredList().get(gameSession.getCurrentTestSheet()) == null)
            gameSession.answerTestSheet(-1);
        // !end of the game
        if (!gameSession.hasNextTestSheet()) {
            handleEnd(update, gameSession);
            return;
        }
        utilityService.sendMessage(utilityService.makePoll(gameSession.getNextTestSheet(), gameSession.getTestIndex(), update.getCallbackQuery().getMessage().getChatId()));
    }

    private void handleEnd(Update update, GameSession gameSession) {
        gameSession.stop();


        utilityService.sendMessage(
                SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId())
                        .text(utilityService.makeResults(gameSession.getAnsweredList()))
                        .replyMarkup(
                                InlineKeyboardMarkup.builder().keyboardRow(
                                        Collections.singletonList(
                                                InlineKeyboardButton.builder().text("Back").callbackData(BACK_BUTTON + TO_CATEGORY).build()
                                        )
                                ).build()
                        ).build()
        );
    }

    public BotApiMethod<?> handlePollAnswer(Update update) {
        PollAnswer pollAnswer = update.getPollAnswer();
        if (pollAnswer.getUser() == null) return null;

        GameSession game = getGame(pollAnswer.getUser().getId());
        if (game == null) return null;

        game.answerTestSheet(pollAnswer.getOptionIds().get(0));
        return null;
    }

    private BotApiMethod<?> sendLanguages(Long chatId, Integer messageId) {

        List<List<InlineKeyboardButton>> matrixButton = new ArrayList<>();

        matrixButton.add(Collections.singletonList(InlineKeyboardButton.builder().text("English  -> Japanese").callbackData(0 + LANGUAGE_POSTFIX + TWGE_SUFFIX).build()));
        matrixButton.add(Collections.singletonList(InlineKeyboardButton.builder().text("Japanese -> English").callbackData(1 + LANGUAGE_POSTFIX + TWGE_SUFFIX).build()));
        matrixButton.add(Collections.singletonList(InlineKeyboardButton.builder().text("English  -> Uzbek").callbackData(2 + LANGUAGE_POSTFIX + TWGE_SUFFIX).build()));
        matrixButton.add(Collections.singletonList(InlineKeyboardButton.builder().text("Uzbek    -> English").callbackData(3 + LANGUAGE_POSTFIX + TWGE_SUFFIX).build()));
        matrixButton.add(Collections.singletonList(InlineKeyboardButton.builder().text("Japanese -> Uzbek").callbackData(4 + LANGUAGE_POSTFIX + TWGE_SUFFIX).build()));
        matrixButton.add(Collections.singletonList(InlineKeyboardButton.builder().text("Uzbek    -> Japanese").callbackData(5 + LANGUAGE_POSTFIX + TWGE_SUFFIX).build()));

        if (messageId != null)
            return EditMessageText.builder()
                    .text("select language")
                    .chatId(chatId)
                    .messageId(messageId)
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(matrixButton).build())
                    .build();

        return SendMessage.builder()
                .text("select language")
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(matrixButton).build())
                .build();
    }

    private BotApiMethod<?> sendCategories(Long chatId, List<Category> categories, List<Integer> selectedCategories, Integer messageId) {

        List<List<InlineKeyboardButton>> preparedGridMarkup = utilityService.makeCategoryGrid(categories, selectedCategories);

        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(preparedGridMarkup)
                .build();

        if (messageId != null)
            return EditMessageText.builder()
                    .replyMarkup(keyboardMarkup)
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("choose categories:")
                    .build();

        return SendMessage.builder()
                .replyMarkup(keyboardMarkup)
                .chatId(chatId)
                .text("choose Categories:")
                .build();
    }


    public boolean lookUpGame(long user_id) {
        return gameSessions.containsKey(user_id);
    }

    public boolean lookUpGamebyMessageId(long message_id) {
        for (GameSession value : gameSessions.values()) {
            if (value.getMessage_id() == message_id) return true;
        }
        return false;
    }

    public void createGame(long user_id, long chat_id) {
        gameSessions.put(user_id, GameSession.builder().user_id(user_id).chat_id(chat_id).build());
    }

    public GameSession getGame(long user_id) {
        return gameSessions.get(user_id);
    }

    public List<Category> getCategories(long user_id) {
        GameSession gameSession = getGame(user_id);
        if (gameSession == null)
            return null;
        return gameSession.getCategories();
    }

    public void toggleCategory(long user_id, Integer category_id) {
        GameSession gameSession = getGame(user_id);
        if (gameSession == null)
            return;
        final var gameCategories = gameSession.getSelectedCategories();
        if (gameCategories.contains(category_id))
            gameCategories.remove(category_id);
        else gameCategories.add(category_id);
    }


    public List<Integer> getSelectedCategories(Long user_id) {
        GameSession gameSession = getGame(user_id);
        if (gameSession == null)
            return null;
        return gameSession.getSelectedCategories();
    }

    // confirmed button, game starts
    public void startGame(Update update) {

        deleteMessage(update.getCallbackQuery().getMessage().getMessageId(),
                update.getCallbackQuery().getMessage().getChatId());

        final long user_id = update.getCallbackQuery().getFrom().getId();
        final long chat_id = update.getCallbackQuery().getMessage().getChatId();
        GameSession gameSession = gameSessions.get(user_id);
        if (gameSession == null)
        {
            utilityService.sendMessage(sendStartMessages(update));
            return;
        }


        Set<TestSheet> testSheets = dictionaryService.fetchTestSheet(10, new HashSet<>(gameSession.getSelectedCategories()), gameSession.getChosenLanguages());

        if  (testSheets == null)
            return;

        gameSession.setTests(testSheets.iterator());

        gameSession.start();
        try {
            utilityService.sendMessage(
                    DeleteMessage.builder()
                            .chatId(gameSession.getChat_id())
                            .messageId(gameSession.getMessage_id())
                            .build()
            );
        } catch (Exception ignore) {
        }
        utilityService.sendMessage(utilityService.makePoll(gameSession.getNextTestSheet(), gameSession.getTestIndex(), chat_id));
    }

    public BotApiMethod<?> sendStartMessages(Update update) {

        String message = "Hello User!" +
                "\n\nWould you like to train your memorising skills?";
        List<InlineKeyboardButton> keyButtons = new ArrayList<>();

        keyButtons.add(
                InlineKeyboardButton.builder()
                        .text("Play")
                        .callbackData(PLAY_BUTTON)
                        .build()
        );

        if (update.getCallbackQuery() != null)
            return EditMessageText.builder()
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .text(message)
                    .replyMarkup(
                            InlineKeyboardMarkup.builder()
                                    .keyboardRow(keyButtons)
                                    .build()
                    )
                    .build();
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(message)
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboardRow(keyButtons)
                                .build()
                )
                .protectContent(true)
                .build();
    }


    public void handleChosenLanguage(Update update) {
        int index = Integer.parseInt(update.getCallbackQuery().getData().substring(0, 1));
        GameSession game = getGame(update.getCallbackQuery().getFrom().getId());
        if (game == null)
        {
            createGame(update.getMessage().getFrom().getId(), update.getCallbackQuery().getMessage().getChatId());
            game = getGame(update.getMessage().getFrom().getId());
        }
        List<Category> dbCategories = categoryService.getAllCategories();
        switch (index) {
            case 0: // eng to japanese
                game.setChosenLanguages(new String[]{WORD_EN, WORD_JP});
                utilityService.sendMessage(sendCategories(update.getCallbackQuery().getMessage().getChatId(), dbCategories, null, update.getCallbackQuery().getMessage().getMessageId()));
                break;
            case 1: // japanese to eng
                game.setChosenLanguages(new String[]{WORD_JP, WORD_EN});
                utilityService.sendMessage(sendCategories(update.getCallbackQuery().getMessage().getChatId(), dbCategories, null, update.getCallbackQuery().getMessage().getMessageId()));
                break;
            case 2: // eng to uz
                game.setChosenLanguages(new String[]{WORD_EN, WORD_UZ});
                utilityService.sendMessage(sendCategories(update.getCallbackQuery().getMessage().getChatId(), dbCategories, null, update.getCallbackQuery().getMessage().getMessageId()));
                break;
            case 3: // uz to eng
                game.setChosenLanguages(new String[]{WORD_UZ, WORD_EN});
                utilityService.sendMessage(sendCategories(update.getCallbackQuery().getMessage().getChatId(), dbCategories, null, update.getCallbackQuery().getMessage().getMessageId()));
                break;
            case 4: // japanese to uz
                game.setChosenLanguages(new String[]{WORD_JP, WORD_UZ});
                utilityService.sendMessage(sendCategories(update.getCallbackQuery().getMessage().getChatId(), dbCategories, null, update.getCallbackQuery().getMessage().getMessageId()));
                break;
            case 5: // uz to japanese
                game.setChosenLanguages(new String[]{WORD_UZ, WORD_JP});
                utilityService.sendMessage(sendCategories(update.getCallbackQuery().getMessage().getChatId(), dbCategories, null, update.getCallbackQuery().getMessage().getMessageId()));
                break;
        }
    }

    public void handleBack(Update update) {
        Long user_id = update.getCallbackQuery().getFrom().getId();

        GameSession game = getGame(user_id);
        if (game == null) {
            utilityService.sendMessage(sendStartMessages(update));
            return;}

        if (update.getCallbackQuery().getData().endsWith(TO_CATEGORY)) {
            utilityService.sendMessage(
                    sendCategories(update.getCallbackQuery().getMessage().getChatId()
                            , game.getCategories(), game.getSelectedCategories(), update.getCallbackQuery().getMessage().getMessageId()));
        } else if (update.getCallbackQuery().getData().endsWith(TO_LANG)) {
            utilityService.sendMessage(
                    sendLanguages(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId())
            );
        }
    }

    public void handleCategoryButtons(Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();
        final String cat_extract = callbackQuery.getData().substring(WORD_CAT_PREFIX.length());

        if (lookUpGame(callbackQuery.getFrom().getId()))
            try {
                int cat_id = Integer.parseInt(cat_extract);

                toggleCategory(callbackQuery.getFrom().getId(), cat_id);

                List<Category> categories = getCategories(callbackQuery.getFrom().getId());
                List<Integer> selectedCategory_ids = getSelectedCategories(callbackQuery.getFrom().getId());


//                EditMessageReplyMarkup editMarkUp = EditMessageReplyMarkup.builder()
//                        .chatId(callbackQuery.getMessage().getChatId())
//                        .messageId(callbackQuery.getMessage().getMessageId())
//                        .replyMarkup(InlineKeyboardMarkup.builder()
//                                .keyboard(utilityService.makeCategoryGrid(categories, selectedCategory_ids))
//                                .build())
//                        .build();

                utilityService.sendMessage(
                        sendCategories(callbackQuery.getMessage().getChatId(), categories,
                                selectedCategory_ids, callbackQuery.getMessage().getMessageId())
                );
//            return editMarkUp;
//            botMain.getMyBot().execute(editMarkUp);

            } catch (NumberFormatException ignored) {
            }
    }
}
