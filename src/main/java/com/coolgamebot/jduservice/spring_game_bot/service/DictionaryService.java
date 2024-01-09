package com.coolgamebot.jduservice.spring_game_bot.service;

import com.coolgamebot.jduservice.spring_game_bot.bot.game.payload.TestSheet;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.Dictionary;
import com.coolgamebot.jduservice.spring_game_bot.entity.lang_entity.*;
import com.coolgamebot.jduservice.spring_game_bot.payload.DictionaryPayload;
import com.coolgamebot.jduservice.spring_game_bot.payload.DictionaryWrap;
import com.coolgamebot.jduservice.spring_game_bot.payload.WordWrap;
import com.coolgamebot.jduservice.spring_game_bot.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.random.RandomGenerator;

import static com.coolgamebot.jduservice.spring_game_bot.bot.Constants.*;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final CategoryService categoryService;
    private final WordService wordService;
    private final RandomGenerator random = RandomGenerator.of("L64X128MixRandom");

    public boolean addWordsEn(WordWrap wordWrap) {
        if (wordWrap == null || wordWrap.getNames().length < 1 || wordWrap.getCategory() == null || wordWrap.getCategory().length() < 1)
            return false;
        Category category = categoryService.addCategory(Category.builder()
                .name(wordWrap.getCategory())
                .build());

        Set<Category> singleton = Collections.singleton(category);
        for (String wordName : wordWrap.getNames()) {
            if (wordName.length() < 1) continue;
            wordService.addWordEn(WordEN.builder()
                    .categories(singleton)
                    .idea(wordName)
                    .build());
        }
        return true;
    }


    public boolean addWordsUz(WordWrap wordWrap) {
        if (wordWrap == null || wordWrap.getNames().length < 1 || wordWrap.getCategory() == null)
            return false;
        Category category = categoryService.addCategory(Category.builder()
                .name(wordWrap.getCategory())
                .build());

        Set<Category> singleton = Collections.singleton(category);
        for (String wordName : wordWrap.getNames()) {
            wordService.addWordUz(WordUZ.builder()
                    .categories(singleton)
                    .idea(wordName)
                    .build());
        }
        return true;
    }


    public boolean addWordsJp(WordWrap wordWrap) {
        if (wordWrap == null || wordWrap.getNames().length < 1 || wordWrap.getCategory() == null)
            return false;
        Category category = categoryService.addCategory(Category.builder()
                .name(wordWrap.getCategory())
                .build());

        Set<Category> singleton = Collections.singleton(category);
        for (String wordName : wordWrap.getNames()) {
            wordService.addWordJp(WordJP.builder()
                    .categories(singleton)
                    .idea(wordName)
                    .build());
        }
        return true;
    }

    public List<WordEN> getWordsEn() {
        return wordService.getWordsEn();
    }

    public List<WordUZ> getWordsUz() {
        return wordService.getWordsUz();
    }

    public List<WordJP> getWordsJp() {
        return wordService.getWordsJp();
    }


    public boolean addDictionaries(DictionaryPayload dictionaryPayload) {
        for (DictionaryWrap dictionaryWrap : dictionaryPayload.getDictionaryWraps()) {
            try {
                addDictionary(dictionaryWrap);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean addDictionary(DictionaryWrap dictionaryWrap) {
        Set<WordEN> wordENS = new HashSet<>();
        Set<WordUZ> wordUZS = new HashSet<>();
        Set<WordJP> wordJPS = new HashSet<>();
        Set<Category> categories = new HashSet<>();

        if (dictionaryWrap.getWord_cat() == null || dictionaryWrap.getWord_en() == null ||
                dictionaryWrap.getWord_uz() == null || dictionaryWrap.getWord_jp() == null)
            return false;
        for (String category : dictionaryWrap.getWord_cat()) {
            categories.add(categoryService.addCategory(Category.builder().name(category).build()));
        }

        for (String wordEn : dictionaryWrap.getWord_en()) {
            wordENS.add(wordService.addWordEn(WordEN.builder().idea(wordEn)
                    .categories(categories)
                    .build()));
        }
        for (String wordUz : dictionaryWrap.getWord_uz()) {
            wordUZS.add(wordService.addWordUz(WordUZ.builder().idea(wordUz)
                    .categories(categories)
                    .build()));
        }
        for (String wordJp : dictionaryWrap.getWord_jp()) {
            wordJPS.add(wordService.addWordJp(WordJP.builder().idea(wordJp)
                    .categories(categories)
                    .build()));
        }
        dictionaryRepository.save(Dictionary.builder()
                .category(categories)
                .id(dictionaryWrap.getId())
                .wordEN(wordENS)
                .wordUZ(wordUZS)
                .wordJP(wordJPS)
                .build());
        return true;
    }

    public List<Dictionary> getPreparedTestDictionaries() {
        return dictionaryRepository.findAll();
    }


    /**
     * for a game to start;
     *
     * @param size       the amount of tests asked;
     * @param categories the test will be prepared from selected categories
     * @param lang       [0]     first language; this language will be used in answer and @optionList
     *                   lang [1]     second language; this langauge will be used in question and the user needs to find this word in lang1
     * @return returns ready sheet list to gameSession and GameEngine will take off from there
     */
    public Set<TestSheet> fetchTestSheet(int size, Set<Integer> categories, String[] lang) {
//        Dictionary [] d = new Dictionary[1];

        List<Long> ids = dictionaryRepository.findAllIdsByCategoryAndLimit(((int) (size * 1.5)), categories);

        if (ids.size() < 4) return null;


        int limit = ids.size() > size ? size : ids.size();

        Map<Long, List<Long>> testIdSheet = new HashMap<>();

        Set<Integer> excluded = new HashSet<>();


//        Collections.shuffle(ids);
        for (int i = 0; i < limit; i++) {
//            if (allById.size() - i < 3) break;

//            Dictionary dictionary = allById.get(i);

//            List<String> options = new ArrayList<>();
            //clear the options before adding another
            List<Long> options = new ArrayList<>();

            Long answerId = ids.get(i);
            options.add(ids.get(i));

            excluded.clear();
            excluded.add(i);
            int randomExcluded = generateRandomNumber(0, ids.size(), excluded);
            excluded.add(randomExcluded);
            options.add(ids.get(randomExcluded));
//            options.add(getExpectedLanguageIdea(lang[1], ids.get(randomExcluded)));

            randomExcluded = generateRandomNumber(0, ids.size(), excluded);
            excluded.add(randomExcluded);
//            options.add(getExpectedLanguageIdea(lang[1], ids.get(randomExcluded)));
            options.add(ids.get(randomExcluded));

            randomExcluded = generateRandomNumber(0, ids.size(), excluded);
            options.add(ids.get(randomExcluded));
//            options.add(getExpectedLanguageIdea(lang[1], ids.get(randomExcluded)));

            testIdSheet.put(answerId, options);
        }
        return getPreparedTestDictionaries(lang, testIdSheet);
    }

    private String getExpectedLanguageIdea(String lan, Dictionary dictionary) {
        switch (lan) {
            case WORD_EN:
                return ((WordEN) getRandom(dictionary.getWordEN().toArray())).getIdea();
            case WORD_UZ:
                return ((WordUZ) getRandom(dictionary.getWordUZ().toArray())).getIdea();
            case WORD_JP:
                return ((WordJP) getRandom(dictionary.getWordJP().toArray())).getIdea();

        }
        return null;
    }

    private int getRandomExcluded(int endRange, List<Integer> excludedList) {
        int n = random.nextInt(endRange - 1);

        for (int exc : excludedList) {
            if (n == exc)
                n++;
        }
        return n;
    }

    public int generateRandomNumber(int min, int max, Set<Integer> excludedSet) {
        if (max <= min) {
            throw new IllegalArgumentException("Invalid range. Max should be greater than min.");
        }

//        if (excludedSet.size() >= (max - min + 1)) {
//            throw new IllegalArgumentException("Cannot generate a random number with the given constraints.");
//        }

//        Random random = new Random();
        int randomNumber;

        do {
            randomNumber = random.nextInt(max);
        } while (excludedSet.contains(randomNumber));

        return randomNumber;
    }

    private int pickRandomNumber(int min, int max, int[] excludeArray) {
        int rangeSize = max - min + 1;

        // Remove excluded numbers from the range
        for (int num : excludeArray) {
            if (num >= min && num <= max) {
                rangeSize--;
            }
        }

        // Pick a random number within the modified range
        if (rangeSize > 0) {
            int randomNumber;
            do {
                randomNumber = random.nextInt(rangeSize) + min;
            } while (Arrays.binarySearch(excludeArray, randomNumber) >= 0);
            return randomNumber;
        } else {
            throw new IllegalArgumentException("No available numbers in the range after exclusions.");
        }
    }

    private Object getRandom(Object[] array) {
        int rnd = random.nextInt(array.length);
        return array[rnd];
    }

    private Set<TestSheet> getPreparedTestDictionaries(String[] lang, Map<Long, List<Long>> callBackIds) {

        Set<TestSheet> fetchData = new HashSet<>();

        callBackIds.forEach((key, value) -> {
            List<String> options = new ArrayList<>();
            Dictionary dictionary = dictionaryRepository.findById(key).orElse(null);
            final String question = getExpectedLanguageIdea(lang[0], dictionary);
            final String answer = getExpectedLanguageIdea(lang[1], dictionary);

            options.add(answer);
            for (int i = 1; i < value.size(); i++) {
                Long dictionaryId = value.get(i);
                Dictionary optionalDictionary = dictionaryRepository.findById(dictionaryId).orElse(null);
                options.add(getExpectedLanguageIdea(lang[1], optionalDictionary));
            }

            Collections.shuffle(options);
            TestSheet testsheet = new TestSheet(question, answer, options.toArray((new String[]{})));
            fetchData.add(testsheet);
        });
        return fetchData;
    }

    public List<Dictionary> getDictionariesByCategoryLimitOffset(String[] categories, Integer limit, Integer offset) {
//        Set<Category> categoriesByNames = categoryService.getCategoriesByNames(categories);
        Page<Dictionary> allByCategory = dictionaryRepository.findAllByCategory_NameIn(Arrays.asList(categories), PageRequest.of(offset, limit));
        return allByCategory.getContent();
    }

    public Object deleteDictionaries(Long[] dictionaryIds) {
        dictionaryRepository.deleteAllByIdInBatch(Arrays.asList(dictionaryIds));
        return true;
    }

    public Object deleteDictionariesByCategory(Integer[] category_ids) {
        dictionaryRepository.deleteAllByCategory_IdIn(Arrays.asList(category_ids));
        return true;
    }
}
