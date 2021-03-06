package com.ust.testutils;

import com.ust.spi.Command;
import com.ust.spi.Entity;
import com.ust.spi.Event;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This helper class can be used to unit test the get/set methods of JavaBean-style Value Objects.
 */
public final class AutoGeneratedClassTester {
    private static int autoIncVal = 0;

    private AutoGeneratedClassTester() {

    }

    /**
     * Test the given class with constructor and getters.
     *
     * @param clazz the class to be tested
     * @return the test status
     */
    public static boolean test(final Class<?> clazz) {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getName().charAt(0) != '$')
                .collect(Collectors.toList());
        Map<String, Field> fieldsMap = fields.stream()
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        Map<String, Object> values = new HashMap<>();
        for (Field field : fields) {
            values.put(field.getName(), getTestValue(field.getType()));
        }

        Object obj = null;
        try {
            obj = clazz.getDeclaredConstructors()[0].newInstance(fields.stream().map(Field::getName)
                    .map(values::get).toArray());
            System.out.println(obj);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Assert.fail("Failed to create the object");
        }

        for (Field field : fieldsMap.values()) {
            try {
                Object value = clazz.getDeclaredMethod(getGetter(field)).invoke(obj);
                Assert.assertEquals(values.get(field.getName()), value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Assert.fail("No such method. [Name=" + getGetter(field) + "]");
            }
        }
        return true;
    }

    private static Object getTestValue(Class<?> clazz) {
        ++autoIncVal;
        if (clazz == String.class) {
            return "testvalue" + autoIncVal;
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return true;
        } else if (clazz == int.class || clazz == Integer.class) {
            return autoIncVal;
        } else if (clazz == long.class || clazz == Long.class) {
            return (long) autoIncVal;
        } else if (clazz == double.class || clazz == Double.class) {
            return 0.5D * autoIncVal;
        } else if (clazz == float.class || clazz == Float.class) {
            return 0.1F * autoIncVal;
        } else if (clazz == char.class || clazz == Character.class) {
            return autoIncVal % 26 + 65;
        } else if (clazz == BigDecimal.class) {
            return new BigDecimal(autoIncVal);
        }
        return null;
    }

    private static String getGetter(Field field) {
        String firstLetterCapital = field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1);
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            return "is" + firstLetterCapital;
        } else {
            return "get" + firstLetterCapital;
        }
    }

    /**
     * This will test command class.
     *
     * @param clazz the class to be tested
     * @return the test status
     */
    public static boolean testCommand(final Class<?> clazz) {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getName().charAt(0) != '$')
                .collect(Collectors.toList());
        Map<String, Field> fieldsMap = fields.stream()
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        Map<String, Object> values = new HashMap<>();
        Object[] valueArr = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            Object testValue = getTestValue(field.getType());
            valueArr[i] = testValue;
            values.put(field.getName(), testValue);
        }

        Command obj = null;
        try {
            obj = (Command) clazz.getDeclaredConstructors()[0].newInstance(valueArr);
            System.out.println(obj);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Assert.fail("Failed to create the object");
        }

        for (Field field : fieldsMap.values()) {
            try {
                Object value = clazz.getDeclaredMethod(getGetter(field)).invoke(obj);
                Assert.assertEquals(values.get(field.getName()), value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Assert.fail("No such method. [Name=" + getGetter(field) + "]");
            }
        }

        Assert.assertEquals(values.get(fields.get(0).getName()), obj.getKey());
        return true;
    }

    /**
     * This will test entity class.
     *
     * @param clazz the class to be tested
     * @return the test status
     * @throws Exception exception when testing
     */
    public static boolean testEntity(final Class<?> clazz) throws Exception {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getName().charAt(0) != '$')
                .collect(Collectors.toList());
        Map<String, Field> fieldsMap = fields.stream()
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        Map<String, Object> values = new HashMap<>();
        for (Field field : fields) {
            values.put(field.getName(), getTestValue(field.getType()));
        }

        List<Class<?>> eventClasses = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals("apply"))
                .map(method -> method.getParameterTypes()[0]).collect(Collectors.toList());

        for (Class<?> eventClass : eventClasses) {
            List<String> eventFields = Arrays.stream(eventClass.getDeclaredFields())
                    .filter(field -> field.getName().charAt(0) != '$').map(Field::getName)
                    .collect(Collectors.toList());
            Event evt = (Event) eventClass.getDeclaredConstructors()[0].newInstance(eventFields.stream()
                    .map(values::get).toArray());
            Entity entity = (Entity) clazz.newInstance();

            Map<String, Object> objValues = new HashMap<>();
            for (Field field : fieldsMap.values()) {
                objValues.put(field.getName(), getGetterValue(field, entity));
            }
            eventFields.forEach(field -> objValues.put(field, values.get(field)));
            entity.applyEvent(evt);
            fieldsMap.values().forEach(field ->
                    Assert.assertEquals(objValues.get(field.getName()), getGetterValue(field, entity)));
            System.out.println(entity);
        }
        return true;
    }

    private static Object getGetterValue(Field field, Object object) {
        try {
            return object.getClass().getDeclaredMethod(getGetter(field)).invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new AutoGeneratedClassTestingException(e);
        }
    }

    /**
     * This will test entity item class.
     *
     * @param clazz the class to be tested
     * @return the test status
     * @throws Exception exception when testing
     */
    public static boolean testEntityItem(final Class<?> clazz) throws Exception {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getName().charAt(0) != '$')
                .collect(Collectors.toList());
        Map<String, Field> fieldsMap = fields.stream()
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        Map<String, Object> values = new HashMap<>();
        for (Field field : fields) {
            values.put(field.getName(), getTestValue(field.getType()));
        }

        Map<Class<?>, Method> eventClasses = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals("apply"))
                .collect(Collectors.toMap(method -> method.getParameterTypes()[0], Function.identity()));

        for (Map.Entry<Class<?>, Method> eventClass : eventClasses.entrySet()) {
            List<String> eventFields = Arrays.stream(eventClass.getKey().getDeclaredFields())
                    .filter(field -> field.getName().charAt(0) != '$').map(Field::getName)
                    .collect(Collectors.toList());
            Event evt = (Event) eventClass.getKey().getDeclaredConstructors()[0].newInstance(eventFields.stream()
                    .map(values::get).toArray());
            Object object = clazz.newInstance();
            Map<String, Object> objValues = new HashMap<>();
            for (Field field : fieldsMap.values()) {
                objValues.put(field.getName(), getGetterValue(field, object));
            }
            eventFields.forEach(field -> objValues.put(field, values.get(field)));
            eventClass.getValue().invoke(object, evt);
            fieldsMap.values().forEach(field ->
                    Assert.assertEquals(objValues.get(field.getName()), getGetterValue(field, object)));
            System.out.println(object);
        }
        return true;
    }
}
