package com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "word_jp")
public class WordJP {
    public static final String SUFFIX = "jp";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 100)
    private String idea;

    @Column
    private String definition;

    @ManyToMany//(mappedBy = "word_jp")
    private Set<Category> categories;
}
