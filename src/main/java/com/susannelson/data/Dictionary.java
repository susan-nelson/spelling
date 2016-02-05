package com.susannelson.data;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable dictionary holding the approved word list.
 * @see com.susannelson.data.Dictionary.DictionaryBuilder
 */
public final class Dictionary {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dictionary.class);

    private final Set<String> words;

    private Dictionary(final Set<String> words) {
        this.words = words;
    }

    /**
     * Returns true if the input is found in the internal list of words.
     *  Since the internal list of words is in lower case, sets the input to lowercase before searching for the word.
     *  Returns false if the input is null or the input string is not found.
     *  @param input - String that represents a word in the dictionary
     *  @return boolean
     */
    public boolean contains(final String input) {

       return (input != null) && words.contains(input.toLowerCase());
    }

    /**
     * Returns Optional containing the input String in lowercase if the input is contained in the internal list of words.
     *  Since the internal list of words is in lower case, sets the input to lowercase before searching for the word.
     *  Return is empty if word is not found or input is null.
     *  @param input - String that represents a word in the dictionary
     *  @return Optional<String> with input in lowercase
     */
    public Optional<String> get(final String input) {

        Optional<String> word = Optional.empty();

        if (input != null) {

            String inputLowerCase = input.toLowerCase();

            if (words.contains(inputLowerCase)) {

                word = Optional.of(inputLowerCase);
            }
        }

        return word;
    }

    /**
     * @return a copy of the internal Set of words.
     */
    public Set<String> getWords() {

        return new HashSet<>(words);
    }

    // for testing
    int size() {
        return words.size();
    }

    /**
     * Returns true if the input is found in the internal list of words.
     * Returns false if the input is null or the input string is not found.
     * Used for testing.
     *  @param input - String that represents a word in the dictionary
     *  @return boolean
     */
    boolean containsCaseSensitive(final String input) {

        return (input != null) && words.contains(input);
    }

    public static class DictionaryBuilder {

        final Set<String> words = new HashSet<>(100000);

        public Dictionary build(final String filePath) {

            InputStream stream = null;
            BufferedReader reader = null;

            try {
                stream = getClass().getResourceAsStream(filePath);
                reader = new BufferedReader(new InputStreamReader(stream));
                String line;

                while ((line = reader.readLine()) != null) {
                    add(line);
                }

                LOGGER.info("Dictionary loaded with " + words.size() + " words.");
                return new Dictionary(words);

            } catch (Exception e) {

                LOGGER.error("Trouble loading Dictionary from file path: " + filePath, e);
                throw new RuntimeException("Not able to populate Dictionary: " + e.getMessage());
            } finally {

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        LOGGER.warn("reader failed to close: " + e.getMessage());
                    }
                }

                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        LOGGER.warn("stream failed to close: " + e.getMessage());
                    }
                }
            }
        }

        public Dictionary build() {

            return new Dictionary(words);
        }

        public void add(final String key) {

            if (!Strings.isNullOrEmpty(key)) {

                words.add(key.toLowerCase());
            }
        }
    }
}
