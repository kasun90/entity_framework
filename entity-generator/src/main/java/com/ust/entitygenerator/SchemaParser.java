package com.ust.entitygenerator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.ust.spi.Command;
import com.ust.spi.Entity;
import com.ust.spi.Event;
import com.ust.spi.MapEntity;
import com.ust.spi.utils.SpiUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This uses to parse the code auto generation schema files.
 */
final class SchemaParser {
    private final String entityPackage;
    private final String eventPackage;
    private final String commandPackage;
    private final BufferedReader reader;

    private ClassInfo entityInfo = null;
    private EventInfo eventInfo = null;
    private ClassInfo commandInfo = null;
    private final Map<String, EventInfo> events = new HashMap<>();

    private final List<ClassInfo> commands = new LinkedList<>();

    private EnumInfo enumInfo = null;
    private final List<EnumInfo> enumInfos = new LinkedList<>();
    private final String enumPackage;

    private ParsingType parsingType = ParsingType.NO_IN;
    private int lineNumber = 0;

    /**
     * The entity parsing type.
     */
    enum ParsingType {
        NO_IN,
        IN_ENTITY,
        IN_MAP_ENTITY,
        IN_MAP_ENTITY_ITEM,
        IN_EVENT,
        IN_COMMAND,
        IN_COMMAND_RESPONSE,
        IN_ENUM
    }

    public SchemaParser(final String entityPackage, final String eventPackage,
                        final String commandPackage, final String enumPackage, final InputStream stream) {
        this.entityPackage = entityPackage;
        this.eventPackage = eventPackage;
        this.commandPackage = commandPackage;
        this.enumPackage = enumPackage;
        reader = new BufferedReader(new InputStreamReader(stream));
    }

    public void writeTo(final String entityDirectory, final String eventDirectory, final String commandDirectory,
                        final String enumDirectory) throws IOException {
        read();
        String srcPath = "/src/main/java";
        String testPath = "/src/test/java";
        if (entityInfo != null) {
            JavaFile file = JavaFile.builder(entityPackage, entityInfo.getBuilder()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(Serializable.class).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(entityDirectory + srcPath).toPath());
            System.out.println("Entity         : " + entityPackage + "." + entityInfo.getClassName());

            file = JavaFile.builder(entityPackage, entityInfo.getTester().addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build())
                    .addStaticImport(Assert.class, "assertTrue").skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(entityDirectory + testPath).toPath());
            System.out.println("EntityTest     : " + entityPackage + "." + entityInfo.getClassName() + "Test");
        }

        if (entityInfo != null && entityInfo.isMapEntity()) {
            JavaFile file = JavaFile.builder(entityPackage, entityInfo.getItemClass().getBuilder()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(Serializable.class).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(entityDirectory + srcPath).toPath());
            System.out.println("EntityItem     : " + entityPackage + "." + entityInfo.getItemClass().getClassName());

            file = JavaFile.builder(entityPackage, entityInfo.getItemClass().getTester()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build())
                    .addStaticImport(Assert.class, "assertTrue")
                    .skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(entityDirectory + testPath).toPath());
            System.out.println("EntityItemTest : " + entityPackage + "." + entityInfo.getItemClass().getClassName()
                    + "Test");
        }

        for (EventInfo eventInfo : events.values()) {
            JavaFile file = JavaFile.builder(eventPackage, eventInfo.getBuilder()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(Serializable.class).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(eventDirectory + srcPath).toPath());
            System.out.println("Event          : " + eventPackage + "." + eventInfo.getName());

            file = JavaFile.builder(eventPackage, eventInfo.getTester()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build())
                    .addStaticImport(Assert.class, "assertTrue")
                    .skipJavaLangImports(true).indent("    ")
                    .build();
            file.writeTo(new File(eventDirectory + testPath).toPath());
            System.out.println("EventTest      : " + eventPackage + "." + eventInfo.getName() + "Test");
        }

        for (ClassInfo command : commands) {
            JavaFile file = JavaFile.builder(commandPackage, command.getBuilder()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(Serializable.class).build())
                    .skipJavaLangImports(true).indent("    ")
                    .build();
            file.writeTo(new File(commandDirectory + srcPath).toPath());
            System.out.println("Command        : " + commandPackage + "." + command.getClassName());
            file = JavaFile.builder(commandPackage, command.getTester()
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build())
                    .addStaticImport(Assert.class, "assertTrue")
                    .skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(commandDirectory + testPath).toPath());
            System.out.println("CommandTest    : " + commandPackage + "." + command.getClassName() + "Test");

            if (command.getItemClass() != null) {
                file = JavaFile.builder(commandPackage, command.getItemClass().getBuilder()
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addSuperinterface(Serializable.class).build()).skipJavaLangImports(true).indent("    ")
                        .build();
                file.writeTo(new File(commandDirectory + srcPath).toPath());
                System.out.println("CommandResponse     : " + commandPackage + "." + command.getItemClass()
                        .getClassName());

                file = JavaFile.builder(commandPackage, command.getItemClass().getTester()
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .build())
                        .addStaticImport(Assert.class, "assertTrue")
                        .skipJavaLangImports(true).indent("    ").build();
                file.writeTo(new File(commandDirectory + testPath).toPath());
                System.out.println("CommandResponseTest : " + commandPackage + "." + command.getItemClass()
                        .getClassName()
                        + "Test");
            }
        }

        for (EnumInfo info : enumInfos) {
            JavaFile file = JavaFile.builder(enumPackage, info.getBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .build())
                    .skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(enumDirectory + srcPath).toPath());
            System.out.println("Enum           : " + enumPackage + "." + info.getName());
        }
    }

    public void read() throws IOException {

        String line;
        parsingType = ParsingType.NO_IN;
        lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            ++lineNumber;
            if (line.isEmpty()) {
                continue;
            }
            if (parsingType == ParsingType.NO_IN) {
                processNotIn(line);
            } else if (parsingType == ParsingType.IN_ENTITY) {
                processInEntity(line);
            } else if (parsingType == ParsingType.IN_COMMAND) {
                processInCommand(line);
            } else if (parsingType == ParsingType.IN_MAP_ENTITY) {
                processInMapEntity(line);
            } else if (parsingType == ParsingType.IN_MAP_ENTITY_ITEM) {
                processInMapEntityItem(line);
            } else if (parsingType == ParsingType.IN_COMMAND_RESPONSE) {
                processInCommandResponse(line);
            } else if (parsingType == ParsingType.IN_ENUM) {
                processInEnum(line);
            } else {
                if ("}".equals(line)) {
                    parsingType = ParsingType.NO_IN;
                    events.put(eventInfo.getName(), eventInfo);
                } else {
                    eventInfo.getFields().add(line.trim());
                }
            }
        }
        if (entityInfo != null) {
            generateEntity(entityInfo, false);
            if (entityInfo.getItemClass() != null) {
                generateEntity(entityInfo.getItemClass(), true);
            }
        }

        commands.forEach(info -> generateCommand(info, false));
        enumInfos.forEach(this::generateEnums);
        events.values().forEach(this::generateEvents);
    }

    private void processInEnum(String line) {
        if ("}".equals(line)) {
            parsingType = ParsingType.NO_IN;
            enumInfos.add(enumInfo);
        } else {
            enumInfo.getItems().add(line.trim());
        }
    }

    private void processInCommandResponse(String line) {
        if ("}".equals(line)) {
            parsingType = ParsingType.IN_COMMAND;
        } else {
            String[] split = line.split(":");
            if (split.length != 2) {
                throw new CodeGenerationException("Failed to process line " + lineNumber + ".\nLine=" + line);
            }
            String[] parts = split[0].split(" ");
            String dataType = parts[0].trim();
            String fieldName = parts[1].trim();
            commandInfo.getItemClass().addField(
                    new FieldInfo(fieldName, dataType, split[1].trim(), commandInfo.getItemClass()));
        }
    }

    private void processInMapEntityItem(String line) {
        if ("}".equals(line)) {
            parsingType = ParsingType.IN_MAP_ENTITY;
        } else {
            String[] split = line.split(":");
            if (split.length != 2) {
                throw new CodeGenerationException("Failed to process line " + lineNumber + ".\nLine=" + line);
            }
            String[] parts = split[0].split(" ");
            String dataType = parts[0].trim();
            String fieldName = parts[1].trim();

            if ("apply".equals(dataType)) {
                entityInfo.getItemClass().getEventApplyInfo()
                        .add(new EventApplyInfo(fieldName, split[1].trim()));
            } else {
                entityInfo.getItemClass().addField(
                        new FieldInfo(fieldName, dataType, split[1].trim(), entityInfo.getItemClass()));
            }
        }
    }

    private void processInMapEntity(String line) {
        if ("}".equals(line)) {
            parsingType = ParsingType.NO_IN;
        } else {
            String[] split = line.split(":");
            if (split.length != 2) {
                throw new CodeGenerationException("Failed to process line " + lineNumber + ".\nLine=" + line);
            }
            String[] parts = split[0].split(" ");
            String dataType = parts[0].trim();
            String fieldName = parts[1].trim();

            switch (dataType) {
                case "apply":
                    entityInfo.getEventApplyInfo().add(new EventApplyInfo(fieldName, split[1].trim()));
                    break;
                case "Item":
                    entityInfo.setItemClass(new ClassInfo());
                    entityInfo.getItemClass().setClassName(fieldName);
                    parsingType = ParsingType.IN_MAP_ENTITY_ITEM;
                    entityInfo.getItemClass().setJavaDoc(getJavaDoc(line));
                    break;
                default:
                    entityInfo.addField(new FieldInfo(fieldName, dataType, split[1].trim(), entityInfo));
                    break;
            }
        }
    }

    private void processInCommand(String line) {
        if ("}".equals(line)) {
            commands.add(commandInfo);
            parsingType = ParsingType.NO_IN;
        } else {
            String[] split = line.split(":");
            if (split.length != 2) {
                throw new CodeGenerationException("Failed to process line " + lineNumber + ".\nLine=" + line);
            }
            String[] parts = split[0].split(" ");
            String dataType = parts[0].trim();
            String fieldName = parts[1].trim();
            if ("Response".equals(dataType)) {
                commandInfo.setItemClass(new ClassInfo());
                commandInfo.getItemClass().setClassName(fieldName);
                parsingType = ParsingType.IN_COMMAND_RESPONSE;
                commandInfo.getItemClass().setJavaDoc(getJavaDoc(line));
            } else {
                commandInfo.addField(new FieldInfo(fieldName, dataType, split[1].trim(), commandInfo));
            }
        }
    }

    private void processInEntity(String line) {
        if ("}".equals(line)) {
            parsingType = ParsingType.NO_IN;
        } else {
            String[] split = line.split(":");
            if (split.length != 2) {
                throw new CodeGenerationException("Failed to process line " + lineNumber + ".\nLine=" + line);
            }
            String[] parts = split[0].split(" ");
            String dataType = parts[0].trim();
            String fieldName = parts[1].trim();

            if ("apply".equals(dataType)) {
                entityInfo.getEventApplyInfo().add(new EventApplyInfo(fieldName, split[1].trim()));
            } else {
                entityInfo.addField(new FieldInfo(fieldName, dataType, split[1].trim(), entityInfo));
            }
        }
    }

    private void processNotIn(String line) {
        String[] split = line.split(":");
        if (split.length != 2) {
            throw new CodeGenerationException("Failed to process line " + lineNumber + ".\nLine=" + line);
        }
        String[] typeData = split[0].split(" ");
        if ("Entity".equals(typeData[0].trim())) {
            parsingType = ParsingType.IN_ENTITY;
            String[] types = typeData[1].trim().split("<|>");
            entityInfo = new ClassInfo();
            entityInfo.setClassName(types[0].trim());
            entityInfo.setMapEntity(false);
            if (types.length == 2) {
                entityInfo.addParentType(types[1].trim());
            }
            entityInfo.setJavaDoc(getJavaDoc(line));
        } else if ("MapEntity".equals(typeData[0].trim())) {
            parsingType = ParsingType.IN_MAP_ENTITY;
            String[] types = typeData[1].trim().split("<|>");
            entityInfo = new ClassInfo();
            entityInfo.setClassName(types[0].trim());
            entityInfo.setMapEntity(true);
            if (types.length == 2) {
                Arrays.stream(types[1].trim().split(",")).map(String::trim)
                        .forEach(s -> entityInfo.addParentType(s));
            }
            entityInfo.setJavaDoc(getJavaDoc(line));
        } else if ("Command".equals(typeData[0].trim())) {
            parsingType = ParsingType.IN_COMMAND;
            String[] types = typeData[1].trim().split("<|>");
            commandInfo = new ClassInfo();
            commandInfo.setClassName(types[0].trim());

            if (types.length == 2) {
                Arrays.stream(types[1].trim().split(",")).map(String::trim)
                        .forEach(s -> commandInfo.addParentType(s));
            }
            commandInfo.setJavaDoc(getJavaDoc(line));
        } else if ("Enum".equals(typeData[0].trim())) {
            parsingType = ParsingType.IN_ENUM;

            enumInfo = new EnumInfo(typeData[1].trim());
            enumInfo.setJavaDoc(getJavaDoc(line));
        } else if ("Event".equals(typeData[0].trim())) {
            parsingType = ParsingType.IN_EVENT;
            eventInfo = new EventInfo(typeData[1].trim(), getJavaDoc(line));
        }
    }

    private void generateEnums(EnumInfo info) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder(info.getName());
        builder.addJavadoc(info.getJavaDoc() + "\n");
        info.getItems().forEach(builder::addEnumConstant);
        info.setBuilder(builder);
    }

    private void generateEntity(ClassInfo classInfo, boolean item) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(classInfo.getClassName());
        if (item) {
            entityInfo.getItemClass().setBuilder(builder);
        } else {
            entityInfo.setBuilder(builder);
        }

        builder.addJavadoc(classInfo.getJavaDoc() + "\n");

        FieldInfo idField = classInfo.getFields().get(0);

        if (!item) {
            String javaDoc = "Gets the unique identifier of the {@link Entity}.\n\n@return the entity id\n";
            if (classInfo.isMapEntity()) {
                javaDoc = "Gets the unique identifier of the {@link MapEntity}.\n\n@return the entity id\n";
            }
            builder.addMethod(MethodSpec.methodBuilder("getId")
                    .addJavadoc(javaDoc)
                    .addCode("return $L;\n", idField.getFieldName())
                    .addAnnotation(Override.class)
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .build());

            if (classInfo.isMapEntity()) {
                builder.addMethod(MethodSpec.constructorBuilder().addCode("super($T.class);\n",
                        getType(classInfo.getItemClass().getClassName())).addModifiers(Modifier.PUBLIC).build());
            }
        }

        addToString(builder);

        for (FieldInfo fieldInfo : classInfo.getFields()) {
            builder.addField(FieldSpec.builder(getType(fieldInfo.getDataType()),
                    fieldInfo.getFieldName(), Modifier.PRIVATE).build());
            builder.addMethod(MethodSpec.methodBuilder(getGetter(fieldInfo.getDataType(), fieldInfo.getFieldName()))
                    .addJavadoc(fieldInfo.getGetterJavaDoc())
                    .addCode("return $L;\n", fieldInfo.getFieldName())
                    .returns(getType(fieldInfo.getDataType()))
                    .addModifiers(Modifier.PUBLIC)
                    .build());
        }


        addEntityEventHandlers(classInfo, item, builder);

        if (!item) {
            if (classInfo.isMapEntity()) {
                builder.superclass(ParameterizedTypeName.get(ClassName.get(MapEntity.class),
                        classInfo.getParentType().stream().map(ClassName::bestGuess).toArray(TypeName[]::new)));
            } else {
                if (classInfo.getParentType().isEmpty()) {
                    builder.superclass(Entity.class);
                } else {
                    builder.superclass(ParameterizedTypeName.get(ClassName.get(Entity.class),
                            classInfo.getParentType().stream().map(ClassName::bestGuess).toArray(TypeName[]::new)));
                }
            }
        }

        generateEntityTest(classInfo, item);
    }

    private void addToString(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.methodBuilder("toString")
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addJavadoc("Returns a string representation of the object.\n\n"
                        + "@return a string representation of the object\n")
                .addCode("return $T.getToString(this);\n", SpiUtils.class).build());
    }

    private void addEntityEventHandlers(ClassInfo classInfo, boolean item, TypeSpec.Builder builder) {
        for (EventApplyInfo eventApplyInfo : classInfo.getEventApplyInfo()) {
            EventInfo eventInfo = events.get(eventApplyInfo.getEventName());

            StringBuilder eventApplyBuilder = new StringBuilder();
            for (String field : eventInfo.getFields()) {
                if (!item || entityInfo.getItemClass().getFieldInfo(field) != null) {
                    eventApplyBuilder.append("this.").append(field).append(" = event.")
                            .append(getGetter(classInfo.getFieldInfo(field).getDataType(), field)).append("();\n");
                }
            }

            builder.addMethod(MethodSpec.methodBuilder("apply")
                    .addJavadoc(eventApplyInfo.getJavaDoc() + ".\n\n@param event the event to be applied\n")
                    .addParameter(getType(eventPackage + "." + eventInfo.getName()), "event", Modifier.FINAL)
                    .addCode(eventApplyBuilder.toString())
                    .addModifiers(Modifier.PUBLIC)
                    .build());
        }
    }

    private void generateEntityTest(ClassInfo classInfo, boolean item) {
        TypeSpec.Builder tester = TypeSpec.classBuilder(classInfo.getClassName() + "Test");
        tester.addJavadoc("Test case for " + classInfo.getClass() + ".\n");
        if (item) {
            entityInfo.getItemClass().setTester(tester);
        } else {
            entityInfo.setTester(tester);
        }


        String method = "testEntity";
        if (item) {
            method = "testEntityItem";
        }
        tester.addMethod(MethodSpec.methodBuilder("autoTest")
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .addCode("assertTrue($L." + method + "(\n        $L.class));\n",
                        getType("com.ust.testutils.AutoGeneratedClassTester"),
                        getType(entityPackage + "." + classInfo.getClassName()))
                .build());
    }

    private void generateCommand(ClassInfo classInfo, boolean response) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(classInfo.getClassName());

        builder.addJavadoc(classInfo.getJavaDoc() + "\n");

        if (!response) {
            FieldInfo idField = classInfo.getFields().get(0);

            String javaDoc = "Gets the unique identifier of the {@link Command}.\n\n@return the command key\n";

            builder.addMethod(MethodSpec.methodBuilder("getKey")
                    .addJavadoc(javaDoc)
                    .addCode("return $L;\n", idField.getFieldName())
                    .addAnnotation(Override.class)
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .build());
        }

        for (FieldInfo fieldInfo : classInfo.getFields()) {
            builder.addField(FieldSpec.builder(getType(fieldInfo.getDataType()),
                    fieldInfo.getFieldName(), Modifier.PRIVATE, Modifier.FINAL).build());
            builder.addMethod(MethodSpec.methodBuilder(getGetter(fieldInfo.getDataType(), fieldInfo.getFieldName()))
                    .addJavadoc(fieldInfo.getGetterJavaDoc())
                    .addCode("return $L;\n", fieldInfo.getFieldName())
                    .returns(getType(fieldInfo.getDataType()))
                    .addModifiers(Modifier.PUBLIC)
                    .build());
        }

        if (!response) {
            builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(Command.class),
                    classInfo.getParentType().stream().map(ClassName::bestGuess).toArray(TypeName[]::new)));
        }

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        for (FieldInfo fieldInfo : classInfo.getFields()) {
            constructor.addParameter(ParameterSpec.builder(getType(fieldInfo.getDataType()),
                    fieldInfo.getFieldName()).addModifiers(Modifier.FINAL).build());
            constructor.addCode("this." + fieldInfo.getFieldName() + " = " + fieldInfo.getFieldName() + ";\n");
            constructor.addModifiers(Modifier.PUBLIC);
        }
        builder.addMethod(constructor.build());

        addToString(builder);

        classInfo.setBuilder(builder);
        if (classInfo.getItemClass() != null) {
            generateCommand(classInfo.getItemClass(), true);
        }

        generateCommandTest(classInfo, response);
    }

    private void generateCommandTest(ClassInfo classInfo, boolean response) {
        TypeSpec.Builder testBuilder = TypeSpec.classBuilder(classInfo.getClassName() + "Test");
        testBuilder.addJavadoc("Test case for " + classInfo.getClass() + ".\n");
        String method = "test";
        if (!response) {
            method = "testCommand";
        }
        testBuilder.addMethod(MethodSpec.methodBuilder("autoTest")
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .addCode("assertTrue($L." + method + "(\n        $L.class));\n",
                        getType("com.ust.testutils.AutoGeneratedClassTester"),
                        getType(commandPackage + "." + classInfo.getClassName()))
                .build());
        classInfo.setTester(testBuilder);
    }

    private void generateEvents(EventInfo info) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(info.getName());
        builder.superclass(Event.class);
        builder.addJavadoc(info.getJavaDoc() + "\n");
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        for (String s : info.getFields()) {
            FieldInfo fieldInfo = entityInfo.getFieldInfo(s);
            if (entityInfo.isMapEntity() && fieldInfo == null) {
                fieldInfo = entityInfo.getItemClass().getFieldInfo(s);
            }
            builder.addField(FieldSpec.builder(getType(fieldInfo.getDataType()),
                    fieldInfo.getFieldName(), Modifier.PRIVATE, Modifier.FINAL).build());
            builder.addMethod(MethodSpec.methodBuilder(getGetter(fieldInfo.getDataType(), fieldInfo.getFieldName()))
                    .returns(getType(fieldInfo.getDataType()))
                    .addModifiers(Modifier.PUBLIC)
                    .addCode("return $L;\n", fieldInfo.getFieldName())
                    .addJavadoc(fieldInfo.getGetterJavaDoc())
                    .build());
            constructor.addParameter(ParameterSpec.builder(getType(fieldInfo.getDataType()),
                    fieldInfo.getFieldName()).addModifiers(Modifier.FINAL).build());
            constructor.addCode("this." + fieldInfo.getFieldName() + " = " + fieldInfo.getFieldName() + ";\n");
        }
        builder.addMethod(constructor.build());

        addToString(builder);

        info.setBuilder(builder);

        TypeSpec.Builder testBuilder = TypeSpec.classBuilder(info.getName() + "Test");
        testBuilder.addJavadoc("Test case for " + info.getClass() + ".\n");
        testBuilder.addMethod(MethodSpec.methodBuilder("autoTest")
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .addCode("assertTrue($L.test(\n        $L.class));\n",
                        getType("com.ust.testutils.AutoGeneratedClassTester"),
                        getType(eventPackage + "." + info.getName()))
                .build());
        info.setTester(testBuilder);
    }

    private TypeName getType(String name) {
        if (name.indexOf('.') != -1) {
            return ClassName.bestGuess(name);
        }

        switch (name) {
            case "String":
                return TypeName.get(String.class);
            case "int":
                return TypeName.get(int.class);
            case "Integer":
                return TypeName.get(Integer.class);
            case "BigDecimal":
            case "double":
            case "Double":
            case "float":
            case "Float":
                return TypeName.get(BigDecimal.class);
            case "long":
                return TypeName.get(long.class);
            case "Long":
                return TypeName.get(Long.class);
            case "boolean":
                return TypeName.get(boolean.class);
            case "Boolean":
                return TypeName.get(Boolean.class);
            default:
                return ClassName.get(entityPackage, name);
        }
    }

    private String getGetter(String type, String fieldName) {
        if ("boolean".equals(type) || "Boolean".equals(type)) {
            return "is" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
        }
        return "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
    }

    private String getJavaDoc(final String line) {
        String trimmedLine = line.trim();
        int index1 = trimmedLine.indexOf(":");
        String doc = trimmedLine.substring(index1 + 1, trimmedLine.length() - 1).trim();
        if (doc.charAt(doc.length() - 1) != '.') {
            doc = doc + ".";
        }
        return doc;
    }
}
