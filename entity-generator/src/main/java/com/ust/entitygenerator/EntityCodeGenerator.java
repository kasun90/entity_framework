package com.ust.entitygenerator;

import java.io.*;

public class EntityCodeGenerator {

    public static void parseDir(File file, String basePackage, String packageName, String entityPath, String eventPath, String commandPath, String enumPath) throws IOException, ClassNotFoundException {
        if (file.isDirectory()) {
            File[] directoryListing = file.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.isDirectory()) {
                        parseDir(child, basePackage, packageName + "." + child.getName(), entityPath, eventPath, commandPath, enumPath);
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

        SchemaParser parser = new SchemaParser(basePackage + ".entity" + packageName,basePackage + ".events" + packageName, basePackage + ".commands" + packageName, basePackage + ".enums",new FileInputStream(file));
        parser.writeTo(entityPath, eventPath, commandPath, enumPath);
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
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

            switch (split[0]) {
                case "package":
                    packageName = split[1];
                    break;
                case "entity":
                    entityPath = split[1];
                    break;
                case "events":
                    eventPath = split[1];
                    break;
                case "commands":
                    commandPath = split[1];
                    break;
                case "enums":
                    enumPath = split[1];
                    break;
            }
        }

        File dir = new File(args[0]).getParentFile();
        parseDir(dir, packageName, "",System.getProperty("user.dir") + entityPath,
                System.getProperty("user.dir") + eventPath,
                System.getProperty("user.dir") + commandPath,
                System.getProperty("user.dir") + enumPath);
    }
}
