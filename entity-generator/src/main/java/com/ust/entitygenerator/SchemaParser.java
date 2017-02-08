package com.ust.entitygenerator;

import com.squareup.javapoet.*;
import com.ust.spi.Command;
import com.ust.spi.Entity;
import com.ust.spi.Event;
import com.ust.spi.MapEntity;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

public class SchemaParser {
    private final String entityPackage;
    private final String eventPackage;
    private final String commandPackage;
    private final BufferedReader reader;
    TypeSpec.Builder entityBuilder;

    TypeSpec.Builder itemBuilder;
    ClassInfo entityInfo = null;
    EventInfo eventInfo = null;
    ClassInfo commandInfo = null;
    Map<String, EventInfo> events = new HashMap<>();
    List<TypeSpec.Builder> eventBuilders = new LinkedList<>();

    List<TypeSpec.Builder> commandBuilders = new LinkedList<>();
    List<ClassInfo> commands = new LinkedList<>();

    EnumInfo enumInfo = null;
    List<EnumInfo> enumInfos = new LinkedList<>();
    List<TypeSpec.Builder> enumBuilders = new LinkedList<>();
    private String enumPackage;

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

    public SchemaParser(String entityPackage, String eventPackage, String commandPackage, String enumPackage, InputStream stream) {
        this.entityPackage = entityPackage;
        this.eventPackage = eventPackage;
        this.commandPackage = commandPackage;
        this.enumPackage = enumPackage;
        reader = new BufferedReader(new InputStreamReader(stream));
    }

    public void writeTo(String entityDirectory, String eventDirectory, String commandDirectory, String enumDirectory) throws IOException, ClassNotFoundException {
        read();
        String srcPath = "/src/main/java";
        if (entityBuilder != null) {
            JavaFile file = JavaFile.builder(entityPackage, entityBuilder.addModifiers(Modifier.PUBLIC).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(entityDirectory + srcPath).toPath());
        }
        if (entityInfo != null && entityInfo.isMapEntity()) {
            JavaFile file = JavaFile.builder(entityPackage, itemBuilder.addModifiers(Modifier.PUBLIC).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(entityDirectory + srcPath).toPath());
        }

        for (TypeSpec.Builder eventBuilder : eventBuilders) {
            JavaFile file = JavaFile.builder(eventPackage, eventBuilder.addModifiers(Modifier.PUBLIC).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(eventDirectory + srcPath).toPath());
        }

        for (TypeSpec.Builder commandBuilder : commandBuilders) {
            JavaFile file = JavaFile.builder(commandPackage, commandBuilder.addModifiers(Modifier.PUBLIC).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(commandDirectory + srcPath).toPath());
        }

        for (TypeSpec.Builder enumBuilder : enumBuilders) {
            JavaFile file = JavaFile.builder(enumPackage, enumBuilder.addModifiers(Modifier.PUBLIC).build()).skipJavaLangImports(true).indent("    ").build();
            file.writeTo(new File(enumDirectory + srcPath).toPath());
        }
    }

    public void read() throws IOException, ClassNotFoundException {

        String line;
        ParsingType parsingType = ParsingType.NO_IN;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            System.out.println(line);
            if (parsingType == ParsingType.NO_IN) {
                String[] split = line.split(":");
                if (split.length != 2) {
                    System.out.println("Skipped line \"" + line + "\"");
                    continue;
                }
                String[] typeData = split[0].split(" ");
                if (typeData[0].trim().equals("Entity")) {
                    parsingType = ParsingType.IN_ENTITY;
                    String[] types = typeData[1].trim().split("<|>");
                    entityInfo = new ClassInfo();
                    entityInfo.setClassName(types[0].trim());
                    entityInfo.setMapEntity(false);
                    if (types.length == 2) {
                        entityInfo.addParentType(types[1].trim());
                    }
                    entityInfo.setJavaDoc(split[1].split("\\{")[0].trim());
                } else if (typeData[0].trim().equals("MapEntity")) {
                    parsingType = ParsingType.IN_MAP_ENTITY;
                    String[] types = typeData[1].trim().split("<|>");
                    entityInfo = new ClassInfo();
                    entityInfo.setClassName(types[0].trim());
                    entityInfo.setMapEntity(true);
                    if (types.length == 2) {
                        Arrays.stream(types[1].trim().split(",")).map(String::trim)
                                .forEach(s -> entityInfo.addParentType(s));
                    }
                    entityInfo.setJavaDoc(split[1].split("\\{")[0].trim());
                } else if (typeData[0].trim().equals("Command")) {
                    parsingType = ParsingType.IN_COMMAND;
                    String[] types = typeData[1].trim().split("<|>");
                    commandInfo = new ClassInfo();
                    commandInfo.setClassName(types[0].trim());

                    if (types.length == 2) {
                        Arrays.stream(types[1].trim().split(",")).map(String::trim)
                                .forEach(s -> commandInfo.addParentType(s));
                    }
                    commandInfo.setJavaDoc(split[1].split("\\{")[0].trim());
                } else if (typeData[0].trim().equals("Enum")) {
                    parsingType = ParsingType.IN_ENUM;

                    enumInfo = new EnumInfo(typeData[1].trim());
                    enumInfo.setJavaDoc(split[1].split("\\{")[0].trim());
                } else if (typeData[0].trim().equals("Event")) {
                    parsingType = ParsingType.IN_EVENT;
                    eventInfo = new EventInfo(typeData[1].trim(), split[1].split("\\{")[0].trim());
                }
            } else if (parsingType == ParsingType.IN_ENTITY) {
                if (line.equals("}")) {
                    parsingType = ParsingType.NO_IN;
                } else {
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        System.out.println("Skipped line \"" + line + "\"");
                        continue;
                    }
                    String[] parts = split[0].split(" ");
                    String dataType = parts[0].trim();
                    String fieldName = parts[1].trim();

                    if (dataType.equals("apply")) {
                        entityInfo.getEventApplyInfo().add(new EventApplyInfo(fieldName, split[1].trim()));
                    } else {
                        entityInfo.addField(new FieldInfo(fieldName, dataType, split[1].trim(), entityInfo));
                    }
                }
            } else if (parsingType == ParsingType.IN_COMMAND) {
                if (line.equals("}")) {
                    commands.add(commandInfo);
                    parsingType = ParsingType.NO_IN;
                } else {
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        System.out.println("Skipped line \"" + line + "\"");
                        continue;
                    }
                    String[] parts = split[0].split(" ");
                    String dataType = parts[0].trim();
                    String fieldName = parts[1].trim();
                    if (dataType.equals("Response")) {
                        commandInfo.setItemClass(new ClassInfo());
                        commandInfo.getItemClass().setClassName(fieldName);
                        parsingType = ParsingType.IN_COMMAND_RESPONSE;
                        commandInfo.getItemClass().setJavaDoc(split[1].split("\\{")[0].trim());
                    } else {
                        commandInfo.addField(new FieldInfo(fieldName, dataType, split[1].trim(), commandInfo));
                    }
                }
            } else if (parsingType == ParsingType.IN_MAP_ENTITY) {
                if (line.equals("}")) {
                    parsingType = ParsingType.NO_IN;
                } else {
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        System.out.println("Skipped line \"" + line + "\"");
                        continue;
                    }
                    String[] parts = split[0].split(" ");
                    String dataType = parts[0].trim();
                    String fieldName = parts[1].trim();

                    if (dataType.equals("apply")) {
                        entityInfo.getEventApplyInfo().add(new EventApplyInfo(fieldName, split[1].trim()));
                    } else if (dataType.equals("Item")) {
                        entityInfo.setItemClass(new ClassInfo());
                        entityInfo.getItemClass().setClassName(fieldName);
                        parsingType = ParsingType.IN_MAP_ENTITY_ITEM;
                        entityInfo.getItemClass().setJavaDoc(split[1].split("\\{")[0].trim());
                    } else {
                        entityInfo.addField(new FieldInfo(fieldName, dataType, split[1].trim(), entityInfo));
                    }
                }
            } else if (parsingType == ParsingType.IN_MAP_ENTITY_ITEM) {
                if (line.equals("}")) {
                    parsingType = ParsingType.IN_MAP_ENTITY;
                } else {
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        System.out.println("Skipped line \"" + line + "\"");
                        continue;
                    }
                    String[] parts = split[0].split(" ");
                    String dataType = parts[0].trim();
                    String fieldName = parts[1].trim();

                    if (dataType.equals("apply")) {
                        entityInfo.getItemClass().getEventApplyInfo().add(new EventApplyInfo(fieldName, split[1].trim()));
                    } else {
                        entityInfo.getItemClass().addField(new FieldInfo(fieldName, dataType, split[1].trim(), entityInfo.getItemClass()));
                    }
                }
            } else if (parsingType == ParsingType.IN_COMMAND_RESPONSE) {
                if (line.equals("}")) {
                    parsingType = ParsingType.IN_COMMAND;
                } else {
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        System.out.println("Skipped line \"" + line + "\"");
                        continue;
                    }
                    String[] parts = split[0].split(" ");
                    String dataType = parts[0].trim();
                    String fieldName = parts[1].trim();
                    commandInfo.getItemClass().addField(new FieldInfo(fieldName, dataType, split[1].trim(), commandInfo.getItemClass()));
                }
            } else if (parsingType == ParsingType.IN_ENUM) {
                if (line.equals("}")) {
                    parsingType = ParsingType.NO_IN;
                    enumInfos.add(enumInfo);
                } else {
                    enumInfo.getItems().add(line.trim());
                }
            } else {
                if (line.equals("}")) {
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

    private void generateEnums(EnumInfo info) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder(info.getName());
        builder.addJavadoc(info.getJavaDoc() + ".\n");
        info.getItems().forEach(builder::addEnumConstant);
        enumBuilders.add(builder);
    }

    private void generateEntity(ClassInfo classInfo, boolean item) {
        try {
            TypeSpec.Builder builder = TypeSpec.classBuilder(classInfo.getClassName());
            if (item) {
                itemBuilder = builder;
            } else {
                entityBuilder = builder;
            }

            builder.addJavadoc(classInfo.getJavaDoc() + ".\n");

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
                    builder.addMethod(MethodSpec.constructorBuilder().addCode("super($T.class);\n", getType(classInfo.getItemClass().getClassName())).addModifiers(Modifier.PUBLIC).build());
                }
            }

            for (FieldInfo fieldInfo : classInfo.getFields()) {
                builder.addField(FieldSpec.builder(getType(fieldInfo.getDataType()), fieldInfo.getFieldName(), Modifier.PRIVATE).build());
                builder.addMethod(MethodSpec.methodBuilder(getGetter(fieldInfo.getFieldName()))
                        .addJavadoc(fieldInfo.getGetterJavaDoc())
                        .addCode("return $L;\n", fieldInfo.getFieldName())
                        .returns(getType(fieldInfo.getDataType()))
                        .addModifiers(Modifier.PUBLIC)
                        .build());
            }


            for (EventApplyInfo eventApplyInfo : classInfo.getEventApplyInfo()) {
                EventInfo eventInfo = events.get(eventApplyInfo.getEventName());

                StringBuilder eventApplyBuilder = new StringBuilder();
                for (String field : eventInfo.getFields()) {
                    if (!item || entityInfo.getItemClass().getFieldInfo(field) != null) {
                        eventApplyBuilder.append("this.").append(field).append(" = event.").append(getGetter(field)).append("();\n");
                    }
                }

                builder.addMethod(MethodSpec.methodBuilder("apply")
                        .addJavadoc(eventApplyInfo.getJavaDoc() + ".\n\n@param event the event to be applied\n")
                        .addParameter(getType(eventPackage + "." + eventInfo.getName()), "event", Modifier.FINAL)
                        .addCode(eventApplyBuilder.toString())
                        .addModifiers(Modifier.PUBLIC)
                        .build());
            }

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
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void generateCommand(ClassInfo classInfo, boolean response) {
        try {
            TypeSpec.Builder builder = TypeSpec.classBuilder(classInfo.getClassName());

            builder.addJavadoc(classInfo.getJavaDoc() + ".\n");

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
                builder.addField(FieldSpec.builder(getType(fieldInfo.getDataType()), fieldInfo.getFieldName(), Modifier.PRIVATE, Modifier.FINAL).build());
                builder.addMethod(MethodSpec.methodBuilder(getGetter(fieldInfo.getFieldName()))
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
                constructor.addParameter(ParameterSpec.builder(getType(fieldInfo.getDataType()), fieldInfo.getFieldName()).build());
                constructor.addCode("this." + fieldInfo.getFieldName() + " = " + fieldInfo.getFieldName() + ";\n");
            }
            builder.addMethod(constructor.build());
            commandBuilders.add(builder);
            if (classInfo.getItemClass() != null) {
                generateCommand(classInfo.getItemClass(), true);
            }
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void generateEvents(EventInfo info) {
        try {
            TypeSpec.Builder builder = TypeSpec.classBuilder(info.getName());
            builder.superclass(Event.class);
            builder.addJavadoc(info.getJavaDoc() + ".\n");
            MethodSpec.Builder constructor = MethodSpec.constructorBuilder();

            for (String s : info.getFields()) {
                FieldInfo fieldInfo = entityInfo.getFieldInfo(s);
                if (entityInfo.isMapEntity() && fieldInfo == null) {
                    fieldInfo = entityInfo.getItemClass().getFieldInfo(s);
                }
                builder.addField(FieldSpec.builder(getType(fieldInfo.getDataType()), fieldInfo.getFieldName(), Modifier.PRIVATE, Modifier.FINAL).build());
                builder.addMethod(MethodSpec.methodBuilder(getGetter(fieldInfo.getFieldName()))
                        .returns(getType(fieldInfo.getDataType()))
                        .addModifiers(Modifier.PUBLIC)
                        .addCode("return $L;\n", fieldInfo.getFieldName())
                        .addJavadoc(fieldInfo.getGetterJavaDoc())
                        .build());
                constructor.addParameter(ParameterSpec.builder(getType(fieldInfo.getDataType()), fieldInfo.getFieldName()).build());
                constructor.addCode("this." + fieldInfo.getFieldName() + " = " + fieldInfo.getFieldName() + ";\n");
            }
            builder.addMethod(constructor.build());
            eventBuilders.add(builder);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private TypeName getType(String name) throws ClassNotFoundException {
        if (name.indexOf('.') != -1) {
            int i = name.lastIndexOf('.');
            return ClassName.bestGuess(name);
            //return ParameterizedTypeName.get(ClassName.get(name.substring(0, i), name.substring(i + 1)));
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

    private String getGetter(String fieldName) {
        return "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
    }

}
