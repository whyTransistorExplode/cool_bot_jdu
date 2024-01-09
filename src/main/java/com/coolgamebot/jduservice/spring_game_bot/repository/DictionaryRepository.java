package com.coolgamebot.jduservice.spring_game_bot.repository;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Dictionary;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
//    Optional<Dictionary> findAllByWordEN(Long aLong);

    @Transactional
    @Query(value = "SELECT (dictionary.id) FROM dictionary where dictionary.id in (select (dictionary_id) from dictionary_category where category_id in (:categories))" +
            " ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Long> findAllIdsByCategoryAndLimit(int limit, Set<Integer> categories);

    List<Dictionary> findAllByIdIn(Collection<Long> id);

    Page<Dictionary> findAllByCategory_NameIn(List<String> categoryNames, Pageable pageable);

    @Transactional
    void deleteAllByCategory_IdIn(Collection<Integer> category_id);
}
