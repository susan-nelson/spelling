package com.susannelson.data;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DictionaryTest {

    final static String FILE_PATH = "/com/susannelson/data/wordsEn.txt";
    final static String EMPTY_FILE_PATH = "/com/susannelson/data/wordsEmpty.txt";
    final static String NULL_FILE_PATH = "/com/susannelson/data/wordsNull.txt";

    Dictionary classUnderTest = null;
    Dictionary.DictionaryBuilder builder = null;

    @BeforeMethod
    public void setup() {

        classUnderTest = new Dictionary.DictionaryBuilder().build();
        builder = new Dictionary.DictionaryBuilder();
    }

    @Test
    public void testAdd() throws Exception {

        builder.add("hello");

        assertTrue(builder.build().containsCaseSensitive("hello"));
    }

    @Test
    public void testAddTwice() throws Exception {


        builder.add("hello");

        assertTrue(builder.build().containsCaseSensitive("hello"));

        builder.add("hello");

        Dictionary dict = builder.build();

        assertTrue(dict.containsCaseSensitive("hello"));
        assertTrue(dict.size() == 1);
    }

    @Test
    public void testAddCaseInsensitive() throws Exception {


        builder.add("Hello");

        assertTrue(builder.build().containsCaseSensitive("hello"));
    }

    @Test
    public void testAddEmpty() throws Exception {


        builder.add("");

        assertTrue(builder.build().size() == 0);
    }

    @Test
    public void testAddNull() throws Exception {


        builder.add(null);

        assertTrue(builder.build().size() == 0);
    }

    @Test
    public void testContains() throws Exception {

        builder.add("hello");

        assertTrue(builder.build().contains("hello"));
    }

    @Test
    public void testContainsToLowerCase() throws Exception {

        builder.add("HELLO");

        assertTrue(builder.build().contains("hello"));

        builder.add("hello");

        Dictionary dict = builder.build();

        assertTrue(dict.containsCaseSensitive("hello"));
        assertTrue(dict.size() == 1);
    }

    @Test
    public void testGet() throws Exception {

        builder.add("hello");
        Dictionary dict = builder.build();
        assertTrue(dict.get("hello").isPresent());

        String hello = dict.get("hello").get();

        assertTrue(hello.equals("hello"));
        assertTrue(dict.size() == 1);
    }

    @Test
    public void testGetNotFound() throws Exception {

        assertFalse(classUnderTest.get("hello").isPresent());
        assertTrue(classUnderTest.size() == 0);
    }

    @Test
    public void testBuild() throws Exception {

        Dictionary dict = builder.build(FILE_PATH);

        assertTrue(dict.size() > 0);
    }

    @Test
    public void testBuildEmptyFile() throws Exception {

        Dictionary dict = builder.build(EMPTY_FILE_PATH);

        assertTrue(dict.size() == 0);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testBuildNoFile() throws Exception {

        builder.build(NULL_FILE_PATH);
    }
}