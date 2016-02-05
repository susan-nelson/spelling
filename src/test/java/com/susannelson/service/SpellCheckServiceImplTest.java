package com.susannelson.service;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.Set;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SpellCheckServiceImplTest {

    SpellCheckServiceImpl classUnderTest = null;

    @BeforeClass
    public void setup() {

        classUnderTest = new SpellCheckServiceImpl();
    }

    @Test
    public void testGetWord() throws Exception {

        Optional<String> word = classUnderTest.getWordInLowercase("hello");

        assertTrue(word.isPresent());
        assertTrue(word.get().equals("hello"));
    }

    @Test
    public void testGetWordInLowercase() throws Exception {

        Optional<String> word = classUnderTest.getWordInLowercase("HELLO");

        assertTrue(word.isPresent());
        assertTrue(word.get().equals("hello"));
    }

    @Test
    public void testGetWordNotPresent() throws Exception {

        Optional<String> word = classUnderTest.getWordInLowercase("youwontfindit");

        assertFalse(word.isPresent());
    }

    @Test
    public void testGetWordEmpty() throws Exception {

        Optional<String> word = classUnderTest.getWordInLowercase("");

        assertFalse(word.isPresent());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetWordNull() throws Exception {

        classUnderTest.getWordInLowercase(null);
    }

    @Test
    public void testIsCaseCorrect() throws Exception {

        assertTrue(classUnderTest.isCaseCorrect("hello"));
        assertTrue(classUnderTest.isCaseCorrect("HELLO"));
        assertTrue(classUnderTest.isCaseCorrect("Hello"));
        assertTrue(classUnderTest.isCaseCorrect("h"));
    }

    @Test
    public void testIsCaseCorrectFalse() throws Exception {

        assertFalse(classUnderTest.isCaseCorrect("HeLlO"));
        assertFalse(classUnderTest.isCaseCorrect("hELLO"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testsCaseCorrectEmpty() throws Exception {

        classUnderTest.isCaseCorrect("");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testsCaseCorrectNull() throws Exception {

        classUnderTest.isCaseCorrect(null);
    }

    @Test
    public void testGetSpellingSuggestions() throws Exception {

        Set<String> words = classUnderTest.makeSuggestions("BallloN");
        assertTrue(words.size() > 0);
    }

    @Test
    public void testFuzzyMatch() {

        Set<String> words = classUnderTest.fuzzyMatch("BallloN");
        assertTrue(words.size() == 1);
    }

    @Test
    public void testFuzzyMatchOneMatch() {

        Set<String> words = classUnderTest.fuzzyMatch("hellllo");
        assertTrue(words.size() == 1);
    }

    @Test
    public void testFuzzyMatchRepeating() {

        Set<String> words = classUnderTest.fuzzyMatch("balllooooon");
        assertTrue(words.size() == 1);
    }

    @Test
    public void testFuzzyMatchMissingVowel() {

        Set<String> words = classUnderTest.fuzzyMatch("balln");
        assertTrue(words.size() == 1);
    }

    @Test
    public void testFuzzyMatchMissingVowel2() {

        Set<String> words = classUnderTest.fuzzyMatch("bllllLLlln");
        assertTrue(words.size() > 0);
    }

    @Test
    public void testCharAppended() {

        Set<String> words = classUnderTest.charAppended("BalllonN");
        assertTrue(words.size() > 0);
    }
}