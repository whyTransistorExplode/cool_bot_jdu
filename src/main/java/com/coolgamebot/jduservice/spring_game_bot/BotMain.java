package com.coolgamebot.jduservice.spring_game_bot;


import com.coolgamebot.jduservice.spring_game_bot.bot.GameBot;
import com.coolgamebot.jduservice.spring_game_bot.data.InitialData;
import com.coolgamebot.jduservice.spring_game_bot.service.UtilityService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Getter
@AllArgsConstructor
public class BotMain {
//    public static final String WEB_PATH = "https://challenge-yourself-with-us.000webhostapp.com/";
    private GameBot myBot;
    private final InitialData initialData;
    private final UtilityService utilityService;
    public static final Logger LOGGER = LoggerFactory.getLogger(SpringGameBotApplication.class);

    @PostConstruct
    public void init() throws TelegramApiException {
        construct();
        initialData.runAfterSetup();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(myBot);
        utilityService.setMyBot(myBot);
        System.out.println("bot initialized!");
    }

    private void construct(){
//        sessionList = new ArrayList<>();
    }
}
