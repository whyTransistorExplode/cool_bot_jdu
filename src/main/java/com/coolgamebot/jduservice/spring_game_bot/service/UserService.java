package com.coolgamebot.jduservice.spring_game_bot.service;

import com.coolgamebot.jduservice.spring_game_bot.entity.User;
import com.coolgamebot.jduservice.spring_game_bot.enums.Role;
import com.coolgamebot.jduservice.spring_game_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    public User addUser(User user){
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
    public User getUser(User user){
        if (user == null)
            return null;
        return userRepository.findById(user.getId()).orElseGet(() -> addUser(user));
    }
//    public boolean userExist(User user){
//        if (user == null ||user.getUsername()== null|| user.getUsername().length() < 1)
//            return false;
//        userRepository.findById(user.getId()).orElseGet()
//
//    }
}
