package com.susannelson.resource;

import com.google.common.collect.ImmutableSet;
import com.susannelson.service.SpellCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.Set;

@Path("/spelling")
@Component
public class SpellingResource {

    //private static final Logger LOGGER = LoggerFactory.getLogger(SpellingResource.class);

    private final SpellCheckService service;

    @Autowired
    public SpellingResource(SpellCheckService service) {
        this.service = service;
    }

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    /**
     * Handles the case where no word param is included in the URL /spelling/{word}.
     * @throws IllegalArgumentException
     */
    public String missingWord() {

        throw new IllegalArgumentException("Input must be at least one character.");
    }

    @GET
    @Path("/{word}")
    @Produces({MediaType.APPLICATION_JSON})
    /**
     * Checks the spelling of the input word and returns a list of spelling suggestions if not spelled correctly.
     * Uses the SpellCheckService to get the word (in lowercase) from the Dictionary.
     * If the word is found and the case is correct, returns response with correct: true.
     * If the word is found and the case is not correct, returns correct:false and the word from the Dictionary in lowercase.
     * If the word is not found, returns correct:false and a list of suggestions.
     * @see SpellCheckService#isCaseCorrect(String)
     * @see SpellCheckService#getSpellingSuggestions(String)
     * @param word - the word to be checked.
     * @returns SpellingCheckResponse
     * @throws WordNotFoundException if the word is not found and no spelling suggestions are found.
     */
    public SpellingCheckResponse isSpellingCorrect(@PathParam("word") String word) {
        //TODO add input validation
        Optional<String> wordFromDictionary = service.getWordInLowercase(word);

        if (wordFromDictionary.isPresent()) {

            if (service.isCaseCorrect(word)) {

                return new SpellingCheckResponse(true, null);
            } else {
                return new SpellingCheckResponse(false, ImmutableSet.of(wordFromDictionary.get()));
            }
        } else {
            Set<String> suggestions = service.getSpellingSuggestions(word);

            if (!suggestions.isEmpty()) {

                return new SpellingCheckResponse(false, suggestions );
            }

            throw new WordNotFoundException("Word not found: " + word);
        }
    }
}
