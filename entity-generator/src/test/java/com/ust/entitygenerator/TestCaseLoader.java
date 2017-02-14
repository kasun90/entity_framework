package com.ust.entitygenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TestCaseLoader {
    private String input;
    private Map<String, String> output = new HashMap<>();

    public void load(String filename) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        StringBuilder builder = new StringBuilder();
        String fileName = "input";
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                if ("====".equals(line.split(" ")[0])) {
                    if ("input".equals(fileName)) {
                        input = builder.toString();
                    } else {
                        output.put(fileName, builder.toString());
                    }
                    fileName = line.split(" ")[1];
                    builder = new StringBuilder();
                    continue;
                }
                builder.append(line).append("\n");
            }
            output.put(fileName, builder.toString());
        }
    }

    public String getInput() {
        return input;
    }

    public Map<String, String> getOutput() {
        return output;
    }
}
