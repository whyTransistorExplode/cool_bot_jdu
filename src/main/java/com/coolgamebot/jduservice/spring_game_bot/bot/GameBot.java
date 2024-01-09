package com.coolgamebot.jduservice.spring_game_bot.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.coolgamebot.jduservice.spring_game_bot.BotMain.LOGGER;


@Component
//@AllArgsConstructor
public class GameBot extends TelegramLongPollingBot {
    public static final String USERNAME = "|oops bot no name|";
    public static final String BOT_TOKEN = "|!!!|";
    private final BotMassagingRouter botMassagingRouter;


    @Autowired
    public GameBot(BotMassagingRouter botMassagingRouter) {
        super(BOT_TOKEN);
        this.botMassagingRouter = botMassagingRouter;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
//        System.out.println("update received!");
        Long startTime = System.currentTimeMillis();
        BotApiMethod<?> route = null;
        try {
            route = botMassagingRouter.route(update);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        long finish = System.currentTimeMillis() - startTime;
        LOGGER.info("this end took: " + finish + "ms");
        if (route != null)
            execute(route);
    }


    @Override
    public String getBotUsername() {
        return USERNAME;
    }
}
