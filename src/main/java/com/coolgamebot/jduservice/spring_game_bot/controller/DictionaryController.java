package com.coolgamebot.jduservice.spring_game_bot.controller;

import com.coolgamebot.jduservice.spring_game_bot.entity.User;
import com.coolgamebot.jduservice.spring_game_bot.payload.DictionaryPayload;
import com.coolgamebot.jduservice.spring_game_bot.payload.DictionaryWrap;
import com.coolgamebot.jduservice.spring_game_bot.payload.WordWrap;
import com.coolgamebot.jduservice.spring_game_bot.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/dict")
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @PostMapping("/add_word_en")
    public ResponseEntity<Object> addWordsEn(@RequestBody WordWrap wordWrap){

        return ResponseEntity.status(dictionaryService.addWordsEn(wordWrap)?200:403).body(null);
    }
    @PostMapping("/add_word_uz")
    public ResponseEntity<Object> addWordsUz(@RequestBody WordWrap wordWrap){

        return ResponseEntity.status(dictionaryService.addWordsUz(wordWrap)?200:403).body(null);
    }
    @PostMapping("/add_word_jp")
    public ResponseEntity<Object> addWordsJp(@RequestBody WordWrap wordWrap){

        return ResponseEntity.status(dictionaryService.addWordsJp(wordWrap)?200:403).body(null);
    }

    @GetMapping("/get-words_en")
    public ResponseEntity<?> getWordsEn(){
        return ResponseEntity.status(200).body(dictionaryService.getWordsEn());
    }

    @GetMapping("/get-words_uz")
    public ResponseEntity<?> getWordsUz(){
        return ResponseEntity.status(200).body(dictionaryService.getWordsUz());

    }

    @GetMapping("/get-words_jp")
    public ResponseEntity<?> getWordsJp(){
        return ResponseEntity.status(200).body(dictionaryService.getWordsJp());
    }

    @PostMapping("/add_dictionaries")
    public ResponseEntity<?> addDictionaries(@RequestBody List<DictionaryWrap> dictionaryWraps){
        return ResponseEntity.ok(dictionaryService.addDictionaries(new DictionaryPayload(dictionaryWraps)));
    }
    @GetMapping("/get-dictionaries")
    public ResponseEntity<?> getDictionaries(@RequestParam(name = "category") String[] categories, @RequestParam Integer limit, @RequestParam Integer offset){
        return ResponseEntity.ok(dictionaryService.getDictionariesByCategoryLimitOffset(categories, limit, offset));
    }
    @DeleteMapping("/delete-dictionaries")
    public ResponseEntity<?> deleteDictionaries(@RequestParam(name = "dictionary_id") Long[] dictionaryIds){
        return ResponseEntity.ok(dictionaryService.deleteDictionaries(dictionaryIds));
    }
    @DeleteMapping("/delete-dictionaries-by-category-id")
    public ResponseEntity<?> deleteDictionariesByCategory(@RequestParam(name = "category_id") Integer[] category_ids){
        return ResponseEntity.ok(dictionaryService.deleteDictionariesByCategory(category_ids));
    }

    @PostMapping("/add_dictionary")
    public String addDictionary(@ModelAttribute DictionaryWrap dictionaryWrap){
        boolean b = dictionaryService.addDictionary(dictionaryWrap);
        return "redirect:http://localhost:5500/public_html/admin_page/index.html";
    }

}
