package com.coolgamebot.jduservice.spring_game_bot.controller;

import com.coolgamebot.jduservice.spring_game_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cat")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/get-categories")
    public ResponseEntity<?> getCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/delete-categories")
    public ResponseEntity<?> deleteCategories(@RequestParam("category_id") Integer[] categoryIds){
        return ResponseEntity.ok(categoryService.deleteCategories(categoryIds));
    }

}
