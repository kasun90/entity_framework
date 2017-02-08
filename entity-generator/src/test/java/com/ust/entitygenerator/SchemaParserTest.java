package com.ust.entitygenerator;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class SchemaParserTest {

    @Test
    public void simpleTest() throws IOException, ClassNotFoundException {
        String spec = "Entity User<Order> : This represents a user of the system {  \t\t\t\n" +
                "    String userId         : user identifier\n" +
                "    BigDecimal age      : sdfsd sfd sfs df\n" +
                "    int value             : sdf fs fsdf sdf sdf sd\n" +
                "    String email             : sdf fs fsdf sdf sdf sd\n" +
                "    String address             : sdf fs fsdf sdf sdf sd\n" +
                "\n" +
                "    apply UserCreated : dasd asd asd sadasd asdasdasdas\n" +
                "    apply AddressChanged : asd asd asdas dasdas dasd\n" +
                "}\n" +
                "\n" +
                "Event UserCreated : gsdsdgs dfsd fsdfsdfsd {\n" +
                "    userId\n" +
                "    age\n" +
                "    value\n" +
                "    email\n" +
                "}\n" +
                "\n" +
                "Event AddressChanged : gsdsdgs dfsd fsdfsdfsd{\n" +
                "    userId\n" +
                "    address\n" +
                "}\n";
        SchemaParser parser = new SchemaParser( "com.test.entity", "com.test.events", "com.test.commands", "com.test.enums", new ByteArrayInputStream(spec.getBytes()));

        Path test = Files.createTempDirectory("test");

        parser.writeTo(test,test,test,test);
        System.out.println("Written to " + test);
    }
}