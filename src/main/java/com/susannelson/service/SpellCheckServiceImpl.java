package com.susannelson.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.susannelson.data.Dictionary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Spell checking service.
 * Given an input string, checks to see if it exists in a dictionary of correctly spelled words.
 * If not, it returns a list of suggested words
 *  - by removing more than 2 repeated characters and checking the dictionary again
 *  - by matching characters of the input with characters in each word of the dictionary, skipping missed vowels
 *  - by adding any character to the beginning and/or end of the input string
 */
@Service
public class SpellCheckServiceImpl implements SpellCheckService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(SpellCheckServiceImpl.class);
    private final static String FILE_PATH = "/com/susannelson/data/wordsEn.txt";
    private final static char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final static ImmutableSet<Character> VOWELS = ImmutableSet.of('a', 'e', 'i', 'o', 'u');
    private final static Pattern lowerCasePattern = Pattern.compile("\\p{javaLowerCase}+");
    private final static Pattern upperCasePattern = Pattern.compile("\\p{javaUpperCase}+");

    final private Dictionary dict;

    SpellCheckServiceImpl() {
        //TODO get the file from a URL to ensure a current list
        dict = new Dictionary.DictionaryBuilder().build(FILE_PATH);
    }

    /**
     * Returns the word from the Dictionary in lowercase.
     *
     * @param word - input string
     * @return String - word from the contained Dictionary or empty string if not found
     * @throws IllegalArgumentException if the input word is null.
     */
    public Optional<String> getWordInLowercase(final String word) {

        if (word == null){

            throw new IllegalArgumentException("Input must not be null.");
        }

        return dict.get(word);
    }

    /**
     * The case of the characters in the input word are correct if they are
     *      all lowercase, or
     *      all uppercase or
     *      the first character is upper case with the remainder lowercase.
     *
     * @param word - input string with at least one character.
     * @return boolean - true if the case of the input word is correct.
     * @throws IllegalArgumentException if the input word is null or empty.
     */
    public boolean isCaseCorrect(final String word) {

        if (Strings.isNullOrEmpty(word) ){

            throw new IllegalArgumentException("Input must be at least one character.");
        }

        return (word.length() == 1) ||
                lowerCasePattern.matcher(word.substring(1)).matches()||
                upperCasePattern.matcher(word).matches();
    }

    /**
     * Returns a sorted set of spelling suggestions from the Dictionary.
     * Suggestions are built by removing repeating characters from the input and performing two searches:
     *  - a fuzzy match (skipping missing vowels)
     *  - adding a character to the beginning and/or end of the input.
     * TODO this could be much nore robust
     * @param word - input string
     * @return Set<String> - spelling suggestions or an empty set if none found.
     */
    public Set<String> getSpellingSuggestions (final String word) {

        if (!dict.contains(word)) {

            return makeSuggestions(word);
        }

        return Collections.emptySet();
    }

    Set<String> makeSuggestions(final String input) {

        //sorted set
        Set<String> toReturn = new TreeSet<>();

        toReturn.addAll(fuzzyMatch(input));
        toReturn.addAll(charAppended(input));

        return toReturn;
    }

    Set<String> fuzzyMatch(final String input) {

        Set<String> toReturn = new HashSet<>();

        String cleanedInput = removeRepeatedChars(input.toLowerCase(), 2);

        if (dict.contains(cleanedInput)) {
            return ImmutableSet.of(cleanedInput);
        }

        return buildFuzzySuggestionList(toReturn, cleanedInput);
    }

    private Set<String> buildFuzzySuggestionList(Set<String> toReturn, String cleanedInput) {

        Set<String> words = dict.getWords();
        char inputChar;
        char wordChar;
        int wordIndex = 0;
        boolean found = true;

        for (String word : words) {

            if (word.length() < cleanedInput.length()) {

                continue;
            }

            for (int i = 0; i < cleanedInput.length(); i++) {

                inputChar = cleanedInput.charAt(i);
                wordChar = word.charAt(wordIndex);

                if (inputChar != wordChar) {
                    //if the input is missing a vowel or two, it is still a match
                    if (skipMissingVowel(inputChar, wordChar, wordIndex, word.length())) {

                        wordChar = word.charAt(++wordIndex);

                        if (inputChar != wordChar) {

                            if (skipMissingVowel(inputChar, wordChar, wordIndex, word.length())) {

                                wordChar = word.charAt(++wordIndex);
                            }
                        }
                    }
                }

                if (inputChar != wordChar) {

                    found = false;
                    break;
                }

                wordIndex++;

                if (wordIndex == word.length()) {

                    // if the whole input was not searched, check to see if the last character of the input is equal
                    // to the last character the word
                    //TODO need to check all characters in the input that were not compared yet
                    if ((i < (cleanedInput.length() - 1)) && (cleanedInput.charAt(cleanedInput.length() - 1) != wordChar)) {

                        found = false;
                    }

                    break;
                }
            }

            if (found && wordIndex == word.length()) {
                toReturn.add(word);
            }

            wordIndex = 0;
            found = true;
        }

        return toReturn;
    }

    HashSet<String> charAppended(final String input) {

        HashSet<String> toReturn = new HashSet<>();
        String cleanedInput = removeRepeatedChars(input.toLowerCase(), 1);

        for (char front : ALPHABET) {

            String atFront = front + cleanedInput;

            if (dict.contains(atFront)) {
                toReturn.add(atFront);
            }

            for (char end : ALPHABET) {

                String atFrontAndEnd = front + cleanedInput + end;

                if (dict.contains(atFrontAndEnd)) {
                    toReturn.add(atFrontAndEnd);
                }
            }
        }
        for (char end : ALPHABET) {

            String atBack = cleanedInput + end;

            if (dict.contains(atBack)) {
                toReturn.add(atBack);
            }
        }

        return toReturn;
    }

    /**
     * Remove adjacent characters from the input string that repeat more than the allowedRepeatCount.
     */
    private String removeRepeatedChars(String input, int allowedRepeatCount) {

        StringBuilder builder = new StringBuilder();
        char previous;
        char current;
        int previousCount = 0;

        if (input.length() > 0) {

            previous = input.charAt(0);
            builder.append(previous);

            for (int i = 1; i < input.length(); ++i) {

                current = input.charAt(i);

                if (current != previous) {

                    builder.append(current);
                    previous = current;
                    previousCount = 0;
                } else {
                    previousCount++;

                    if (previousCount < allowedRepeatCount) {
                        builder.append(current);
                    }
                }
            }
        }

        return builder.toString();
    }

    private boolean skipMissingVowel(final char inputChar, final char wordChar, final int wordIndex, final int wordLength) {

        if (!VOWELS.contains(inputChar) && VOWELS.contains(wordChar)) {

            if ((wordIndex + 1) < wordLength) {

                return true;
            }
        }

        return false;
    }
}
