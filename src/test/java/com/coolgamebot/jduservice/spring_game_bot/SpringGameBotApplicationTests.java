package com.coolgamebot.jduservice.spring_game_bot;

import com.coolgamebot.jduservice.spring_game_bot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringGameBotApplicationTests {

	@MockBean
	UserRepository userRepository;

	@Test
	void contextLoads() {
		when(userRepository.findAll()).thenReturn(new ArrayList<>());

	}


}
