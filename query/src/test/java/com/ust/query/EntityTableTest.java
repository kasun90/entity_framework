package com.ust.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("PMD")
public class EntityTableTest {

    private Connection connection;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        String file = ClassLoader.getSystemResource("model.json").getFile();
        connection = DriverManager.getConnection("jdbc:calcite:model=" + file, info);
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void test_connection() throws SQLException {
        ResultSet rs
            = connection.getMetaData().getTables(null, null, null, null);
        if (rs.next()) {
            assertEquals("Student", rs.getString(3));
        } else {
            Assert.fail("table not found");
        }

    }

    @Test
    public void get_all_entities_test() throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("select name ,age from Student where name = 'nuwan' and (age = 10 or name = 'sanjeewa' and age = 20) ");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1));
            System.out.println(rs.getInt(2));
        }
//            
//            PreparedStatement stmt  = connection.prepareStatement("select name from Student where age = 10");
//            ResultSet rs = stmt.executeQuery();
//            while(rs.next())
//            {
//                System.out.println(rs.getString(1));
//            }

    }
}
