package com.ust.entitygenerator;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * This generates the entity codes from the schema.
 */
public final class EntityCodeGenerator {
    private EntityCodeGenerator() {

    }

    private static void parseDir(File file, String basePackage, String packageName,
                                 String entityPath, String eventPath, String commandPath, String enumPath)
            throws IOException {
        if (file.isDirectory()) {
            File[] directoryListing = file.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.isDirectory()) {
                        parseDir(child, basePackage,
                                packageName + "." + child.getName(),
                                entityPath, eventPath, commandPath, enumPath);
                    } else {
                        parseDir(child, basePackage, packageName, entityPath, eventPath, commandPath, enumPath);
                    }
                }
            }
            return;
        }

        if (file.getName().equals("autogen.conf")) {
            return;
        }

        SchemaParser parser = new SchemaParser(basePackage + ".entity" + packageName,
                basePackage + ".events" + packageName, basePackage + ".commands" + packageName,
                basePackage + ".enums", new FileInputStream(file));
        parser.writeTo(entityPath, eventPath, commandPath, enumPath);
    }

    /**
     * The main method of the entity code generator.
     *
     * @param args the first argument should be the schema information file.
     * @throws Exception exception while generating classes
     */
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(
                new FileReader((args.length == 1) ? args[0] : "entity-schema/autogen.conf"));
        String line;

        String packageName = "";
        String entityPath = "";
        String eventPath = "";
        String commandPath = "";
        String enumPath = "";

        while ((line = in.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] split = line.split("=");
            if (split.length != 2) {
                continue;
            }

            switch (split[0].trim()) {
                case "package":
                    packageName = split[1].trim();
                    break;
                case "entity":
                    entityPath = System.getProperty("user.dir") + split[1].trim();
                    break;
                case "events":
                    eventPath = System.getProperty("user.dir") + split[1].trim();
                    break;
                case "commands":
                    commandPath = System.getProperty("user.dir") + split[1].trim();
                    break;
                case "enums":
                    enumPath = System.getProperty("user.dir") + split[1].trim();
                    break;
                default:
            }
        }

        File dir = new File(args[0]).getParentFile();

        FileUtils.deleteDirectory(new File(entityPath + "/src/main/java"));
        FileUtils.deleteDirectory(new File(entityPath + "/src/test/java"));
        FileUtils.deleteDirectory(new File(eventPath + "/src/main/java"));
        FileUtils.deleteDirectory(new File(eventPath + "/src/test/java"));
        FileUtils.deleteDirectory(new File(commandPath + "/src/main/java"));
        FileUtils.deleteDirectory(new File(commandPath + "/src/test/java"));
        FileUtils.deleteDirectory(new File(enumPath + "/src/main/java"));
        FileUtils.deleteDirectory(new File(enumPath + "/src/test/java"));
        parseDir(dir, packageName, "", entityPath, eventPath, commandPath, enumPath);
    }
}
