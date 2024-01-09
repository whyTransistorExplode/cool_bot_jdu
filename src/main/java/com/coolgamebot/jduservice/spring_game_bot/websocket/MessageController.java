package com.coolgamebot.jduservice.spring_game_bot.websocket;

import com.coolgamebot.jduservice.spring_game_bot.repository.UserRepository;
import com.coolgamebot.jduservice.spring_game_bot.websocket.payload.PayloadData;
import com.coolgamebot.jduservice.spring_game_bot.websocket.payload.ReceiveMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final
    UserRepository userRepository;


    @MessageMapping("/pv/{id}")
    @SendToUser("/queue/reply")
    public PayloadData startMatchHandler(@Payload ReceiveMessage message, @DestinationVariable String id, Principal principal) throws Exception {

        System.out.println("id: " + id);
        return new PayloadData("Hello, " + message.getStatus() + "!"); //HtmlUtils.htmlEscape()
    }
}
