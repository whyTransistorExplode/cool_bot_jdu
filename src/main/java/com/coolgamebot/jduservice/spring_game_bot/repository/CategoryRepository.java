package com.coolgamebot.jduservice.spring_game_bot.repository;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    Set<Category> findAllByNameIn(Collection<String> name);

}
