package com.susannelson.service;

import java.util.Optional;
import java.util.Set;

public interface SpellCheckService {

    /**
     * Returns the word from the Dictionary in lowercase.
     *
     * @param word - input string
     * @return String - word from the contained Dictionary or empty string if not found
     * @throws IllegalArgumentException if the input word is null.
     */
    Optional<String> getWordInLowercase(String word);

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
    boolean isCaseCorrect(String word);
    Set<String> getSpellingSuggestions (String word);

}
