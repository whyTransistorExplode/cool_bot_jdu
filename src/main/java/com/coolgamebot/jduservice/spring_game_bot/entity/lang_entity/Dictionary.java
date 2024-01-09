package com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<WordEN> wordEN;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<WordJP> wordJP;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<WordUZ> wordUZ;

    @ManyToMany(fetch = FetchType.EAGER)//fetch = FetchType.LAZY
    private Set<Category> category;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

