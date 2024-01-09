package com.coolgamebot.jduservice.spring_game_bot.entity;

import com.coolgamebot.jduservice.spring_game_bot.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class User {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String languageCode;
    private long chatId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updateAt;

    private long score;

//    public static User makeUserFromTGUser(org.telegram.telegrambots.meta.api.objects.User user) {
//        return User.builder()
//                .id(user.getId())
//                .username(user.getUserName())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .languageCode(user.getLanguageCode())
//                .role(Role.USER)
//                .build();
//    }
}