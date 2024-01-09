package com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "word_en")
public class WordEN {
    public static final String SUFFIX = "en";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 100)
    private String idea;

    @Column
    private String definition;

    @ManyToMany(fetch = FetchType.EAGER)//(mappedBy = "word_en")
    private Set<Category> categories;
}
