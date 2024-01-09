package com.coolgamebot.jduservice.spring_game_bot.service;

import com.coolgamebot.jduservice.spring_game_bot.bot.GameBot;
import com.coolgamebot.jduservice.spring_game_bot.bot.game.payload.TestSheet;
import com.coolgamebot.jduservice.spring_game_bot.entity.User;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import com.coolgamebot.jduservice.spring_game_bot.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.coolgamebot.jduservice.spring_game_bot.bot.Constants.*;

@Service
@RequiredArgsConstructor
public class UtilityService {
    private final UserService userService;
    private final static int CATEGORY_GRID_LAYOUT_ROW_LIMIT = 2;
    private GameBot mybot;
//    public List<WordWrap>

    public void setMyBot(GameBot singletonBot) {
        this.mybot = singletonBot;
    }

    @SneakyThrows
    public Serializable sendMessage(BotApiMethod<?> botApiMethod) {
        return this.mybot.execute(botApiMethod);
    }

    public void sendDistributedMessage(String message, long chatId) {
        byte[] returnMessage = message.getBytes(StandardCharsets.UTF_8);
        byte[] buff;
        int startPoint = 0;

        while (startPoint < returnMessage.length) {
            buff = Arrays.copyOfRange(returnMessage, startPoint, startPoint + 1024);
            startPoint += 1024;

            sendMessage(SendMessage.builder()
                    .chatId(chatId)
                    .text(new String(buff, StandardCharsets.UTF_8))
                    .build());
        }
    }

    public Boolean checkUserRoleIn(User user, Role... role) {
        User dbUser = userService.getUser(user);
        boolean flag;
        for (Role role1 : role) {
            flag = dbUser != null && dbUser.getRole().equals(role1);
            if (flag) return true;
        }
        return false;
    }

    public Boolean checkUserRoleInGivenUser(User dbUser, Role... role) {
        boolean flag;
        for (Role role1 : role) {
            if (dbUser.getRole().equals(role1))
                return true;
        }
        return false;
    }

    public User userFromUpdate(org.telegram.telegrambots.meta.api.objects.User user, long chatId) {
        return User.builder()
                .username(user.getUserName())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .id(user.getId())
                .chatId(chatId)
                .role(Role.USER)
                .languageCode(user.getLanguageCode())
                .build();
    }


    public List<List<InlineKeyboardButton>> makeCategoryGrid(List<Category> categories, List<Integer> selectedCategories) {
        if (categories == null)
            return null;
        List<List<InlineKeyboardButton>> columnRowKeys = new ArrayList<>();
        List<InlineKeyboardButton> rowKeys = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            String pref = "";
            if (selectedCategories != null)
                pref = selectedCategories.contains(category.getId()) ? "\u2611" : "";//✓

            rowKeys.add(InlineKeyboardButton.builder()
                    .text(pref + category.getName())
                    .callbackData(WORD_CAT_PREFIX + category.getId().toString())
                    .build());


            if (rowKeys.size() >= CATEGORY_GRID_LAYOUT_ROW_LIMIT || i + 1 >= categories.size()) {
                columnRowKeys.add(rowKeys);
                rowKeys = new ArrayList<>();
            }

        }
        columnRowKeys.add(makeCategoryConfirmButton());
        columnRowKeys.add(makeCategoryBackButton());
        return columnRowKeys;
//        listKeyboards.add(InlineKeyboardButton.builder().text("hello").callbackData("hello_callback").build());
//        listKeyboards.add(InlineKeyboardButton.builder().text("say").callbackData("say_bla").build());
    }

    private List<InlineKeyboardButton> makeCategoryConfirmButton() {
        return new ArrayList<>(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Confirm")
                .callbackData(CONFIRM_BUTTON)
                .build()));
    }

    private List<InlineKeyboardButton> makeCategoryBackButton() {
        return new ArrayList<>(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Back")
                .callbackData(BACK_BUTTON + TO_LANG)
                .build()));
    }

    public SendPoll makePoll(TestSheet testSheet, int index, Long chat_id) {

        List<String> strings = Arrays.asList(testSheet.getOptionList());
        List<InlineKeyboardButton> nextButton = new ArrayList<>();
        nextButton.add(
                InlineKeyboardButton.builder()
                        .text("Next")
                        .callbackData(NEXT_BUTTON)
                        .build()
        );

        return SendPoll.builder()
                .question(index + ".  " + testSheet.getQuestion())
                .options(strings)
                .isAnonymous(false)
                .chatId(chat_id)
                .correctOptionId(strings.indexOf(testSheet.getAnswer()))
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboardRow(nextButton)
                                .build())
                .explanation("Answer is: " + testSheet.getAnswer())
                .allowMultipleAnswers(false)
                .openPeriod(15)
                .type("quiz")
                .build();
    }

    public String makeResults(Map<TestSheet, Integer> answerList) {
        StringBuilder builder = new StringBuilder();
        AtomicInteger index = new AtomicInteger();
        answerList.forEach((key, value) -> {
            index.getAndIncrement();
            if (value == -1)
                builder.append(index).append(". ✘ ").append(" not Answered: ").append(key.getAnswer()).append('\n');
            else if (key.getOptionList()[value].equals(key.getAnswer()))
                builder.append(index).append(". ✅ ").append(" answer: ").append(key.getAnswer()).append('\n');
            else
                builder.append(index).append(". ❌").append(" mistake: ").append(key.getQuestion()).append('\n');
        });//✘ ✔
        return builder.toString();
    }
}
