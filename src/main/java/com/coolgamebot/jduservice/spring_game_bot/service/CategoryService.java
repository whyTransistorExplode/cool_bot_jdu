package com.coolgamebot.jduservice.spring_game_bot.service;

import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Category;
import com.coolgamebot.jduservice.spring_game_bot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category addCategory(Category category){
        if (category.getName().length() < 1) return null;
        return categoryRepository.findByName(category.getName()).orElseGet(() -> categoryRepository.save(category));
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Set<Category> getCategoriesByNames(String[] categories) {
        return categoryRepository.findAllByNameIn(Arrays.asList(categories));
    }

    public Object deleteCategories(Integer[] categoryIds) {
        categoryRepository.deleteAllByIdInBatch(Arrays.asList(categoryIds));
        return true;
    }
}
