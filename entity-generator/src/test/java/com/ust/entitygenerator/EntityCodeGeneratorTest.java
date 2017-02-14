package com.ust.entitygenerator;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;

public class EntityCodeGeneratorTest {
    @Test
    public void main() throws Exception {
        try {
            Constructor<?> constructor = EntityCodeGenerator.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            constructor.newInstance();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("generator/autogen.conf").getFile());
            String load = Files.createTempDirectory("load").toString();
            System.setProperty("user.dir", load);
            EntityCodeGenerator.main(new String[]{file.getAbsolutePath()});
            FileUtils.deleteDirectory(new File(load));
        } catch (Exception ex) {
            Assert.fail();
        }
    }

}