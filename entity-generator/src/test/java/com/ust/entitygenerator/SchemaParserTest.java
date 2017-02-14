package com.ust.entitygenerator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class SchemaParserTest {

    AutogenValidator validator;
    @Before
    public void begin() {
        validator = new AutogenValidator();
    }

    @After
    public void end() throws Exception {
        validator.close();
        validator = null;
    }

    @Test
    public void simpleEntityTest() throws Exception {
        validator.validateFile("simpleEntityTest.txt");
    }

    @Test
    public void evenApplyTest() throws IOException, Exception {
        validator.validateFile("evenApplyTest.txt");
    }

    @Test
    public void mapEntityTest() throws IOException, Exception {
        validator.validateFile("mapEntityTest.txt");
    }

    @Test
    public void commandTest() throws IOException, Exception {
        validator.validateFile("commandTest.txt");
    }


    @Test
    public void enumTest() throws IOException, Exception {
        validator.validateFile("enumTest.txt");
    }

    @Test
    public void entityWithParentTest() throws IOException, Exception {
        validator.validateFile("entityWithParentTest.txt");
    }

    @Test(expected = RuntimeException.class)
    public void invalidLineOuterTest() throws IOException, Exception {
        validator.validateFile("invalidLineOuterTest.txt");
    }

    @Test(expected = RuntimeException.class)
    public void invalidLineEntityTest() throws IOException, Exception {
        validator.validateFile("invalidLineEntityTest.txt");
    }

    @Test(expected = RuntimeException.class)
    public void invalidLineMapEntityTest() throws IOException, Exception {
        validator.validateFile("invalidLineMapEntityTest.txt");
    }

    @Test(expected = RuntimeException.class)
    public void invalidLineMapEntityItemTest() throws IOException, Exception {
        validator.validateFile("invalidLineMapEntityItemTest.txt");
    }

    @Test(expected = RuntimeException.class)
    public void invalidLineCommandResponseTest() throws IOException, Exception {
        validator.validateFile("invalidLineCommandResponseTest.txt");
    }

    @Test(expected = RuntimeException.class)
    public void invalidLineCommandTest() throws IOException, Exception {
        validator.validateFile("invalidLineCommandTest.txt");
    }
}