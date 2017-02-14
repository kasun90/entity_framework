package com.ust.entitygenerator;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class AutogenValidator implements AutoCloseable {
    private String input;
    private String basePath;

    public void validateFile(String filename) throws Exception {
        TestCaseLoader loader = new TestCaseLoader();
        loader.load(filename);
        setInput(loader.getInput());
        generate();
        for (Map.Entry<String, String> entry : loader.getOutput().entrySet()) {
            Assert.assertEquals(entry.getKey(), entry.getValue(), getCode(entry.getKey()));
        }
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void generate() throws IOException {
        SchemaParser parser = new SchemaParser("entity",
                "events",
                "commands",
                "enums",
                new ByteArrayInputStream(input.getBytes()));

        basePath = Files.createTempDirectory("load").toString();
        parser.writeTo(basePath, basePath, basePath, basePath);
        System.out.println("Written to " + basePath);
    }

    public String getCode(String name) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(basePath + "/" + name))) {
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        }
    }

    @Override
    public void close() throws Exception {
        if (basePath != null) {
            FileUtils.deleteDirectory(new File(basePath));
        }
    }
}
