package com.ust.spi.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class SpiUtilsTest {
    static class Cat {
        private final String name;
        private final int age;

        Cat(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    public void getToString() throws Exception {
        Constructor<?> constructor = SpiUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
        Cat cat = new Cat("pusa", 10);
        Assert.assertEquals("{\"name\":\"pusa\",\"age\":10}", SpiUtils.getToString(cat));
    }

}