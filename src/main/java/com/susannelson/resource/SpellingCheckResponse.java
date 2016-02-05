package com.susannelson.resource;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpellingCheckResponse {

    private final boolean correct;
    private final Set<String> suggestions;

    public SpellingCheckResponse(boolean correct, Set<String> suggestions) {
        this.correct = correct;
        this.suggestions = suggestions;
    }

    public boolean isCorrect() {
        return correct;
    }

    public Set<String> getSuggestions() {
        return suggestions;
    }
}
