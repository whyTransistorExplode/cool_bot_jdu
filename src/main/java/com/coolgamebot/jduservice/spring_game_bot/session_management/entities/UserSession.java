package com.coolgamebot.jduservice.spring_game_bot.session_management.entities;

import com.coolgamebot.jduservice.spring_game_bot.bot.game.GameSession;
import com.coolgamebot.jduservice.spring_game_bot.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class UserSession {
    private final long creationTime;
    private long updateTime;
    private final long userId;
    private String uuid;
    private User user;
    private boolean isActive;
    private GameSession gameSession;

    public long getCreationTime() {
        return creationTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void update() {
        this.updateTime = System.currentTimeMillis();
    }

    public void logout() {
        this.updateTime = System.currentTimeMillis();
        this.isActive = false;
    }

    public String getUuid() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
        return uuid;
    }

    private UserSession(long creationTime, long updateTime, long userId, User user, boolean isActive, GameSession gameSession) {
        this.creationTime = creationTime;
        this.updateTime = updateTime;
        this.userId = userId;
        this.user = user;
        this.isActive = isActive;
        this.gameSession = gameSession;
    }

    public static UserSession DefaultUserSession(long userId){
        final long c_time = System.currentTimeMillis();
        return new UserSession(
                c_time, c_time, userId, null, true, null
        );
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "creationTime=" + creationTime +
                ", updateTime=" + updateTime +
                ", userId='" + userId + '\'' +
                ", isActive=" + isActive +
                '}';
    }

}