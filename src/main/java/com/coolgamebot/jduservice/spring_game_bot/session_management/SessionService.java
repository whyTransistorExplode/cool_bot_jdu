package com.coolgamebot.jduservice.spring_game_bot.session_management;

import com.coolgamebot.jduservice.spring_game_bot.session_management.entities.UserSession;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class SessionService {
    private Map<Long, UserSession> sessionList;
    private final long sessionExpireTime = 1800000;

    public SessionService() {
        this.sessionList = new HashMap<>();
    }

    public void removeSession(UserSession session) {
        try {
            sessionList.remove(session.getUserId());
        } catch (RuntimeException e) {
            System.out.println("couldn't remove Session: " + e.getMessage());
        }
    }

    public UserSession getNonExpiredSessionByUsername(String username) {
        UserSession sessionByUsername = getSessionByUsername(username);
        if (sessionByUsername != null && isSessionNotExpired(sessionByUsername))
            return sessionByUsername;
        removeSession(sessionByUsername);
        return null;
    }

    public UserSession getSession(Long user_id) {
        //        if (userSession == null)
//            return UserSession.DefaultUserSession(user_id);

        return sessionList.get(user_id);
    }

    public UserSession createSession(long user_id){
        UserSession userSession = UserSession.DefaultUserSession(user_id);
        sessionList.put(user_id, userSession);
        return userSession;
    }

    public UserSession getSessionByUsername(String username) {
        for (UserSession session : sessionList.values()) {
            if (session.getUser().getUsername().equals(username))
                return session;
        }
        return null;
    }

    private boolean isSessionNotExpired(UserSession session) {
        return (System.currentTimeMillis() - session.getUpdateTime()) < sessionExpireTime;
    }
}
