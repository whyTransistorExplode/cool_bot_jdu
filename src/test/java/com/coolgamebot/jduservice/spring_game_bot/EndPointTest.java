package com.coolgamebot.jduservice.spring_game_bot;

import com.coolgamebot.jduservice.spring_game_bot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 class EndPointTest {

    @MockBean
    UserRepository userRepository;

    @Test
    void contextLoad() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
    }



}
