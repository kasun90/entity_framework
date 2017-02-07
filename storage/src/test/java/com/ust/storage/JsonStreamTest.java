package com.ust.storage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ust.storage.query.JsonHelper;
import com.ust.storage.query.JsonStream;
import static com.ust.storage.query.JsonStream.ASC;
import static com.ust.storage.query.JsonStream.DESC;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rocksdb.RocksDBException;

@SuppressWarnings("PMD")
public class JsonStreamTest {

    private JsonStream jsonStream;
    private final List<JsonObject> jsonList = new LinkedList<>();

    @Before
    public void setUp() throws RocksDBException, IOException {
        loadData();
        jsonStream = new JsonStream(jsonList.iterator());
    }

    @After
    public void tearDown() {
        jsonList.clear();
    }

    private void loadData() throws FileNotFoundException, IOException {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader("target/classes/data.json"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                JsonObject json = gson.fromJson(line, JsonObject.class);
                jsonList.add(json);
            }
        }
    }

    @Test
    public void stream_filter_stream_source_test() {
        jsonStream = new JsonStream(JsonHelper.stream(jsonList.iterator()));
        List<JsonObject> output = jsonStream.filter("city", "GALLE").toList();
        output.stream().forEach(json -> check_value(json, "city", "GALLE"));
    }
    
    @Test
    public void stream_filter_test() {
        List<JsonObject> output = jsonStream.filter("city", "GALLE").toList();
        output.stream().forEach(json -> check_value(json, "city", "GALLE"));
    }

    @Test
    public void stream_filter_limit_test() {
        List<JsonObject> output = jsonStream.filter("city", "GALLE").limit(2).toList();
        Assert.assertEquals(2, output.size());
        output.stream().forEach(json -> check_value(json, "city", "GALLE"));
    }
    
    @Test
    public void stream_orderby_single_field_test() {
        List<JsonObject> output = jsonStream.orderBy("city").toList();
        check_list(output.stream(), "city", "COLOMBO", "COLOMBO",
            "COLOMBO", "GALLE", "GALLE", "GALLE",
            "NUGEGODA", "NUGEGODA");
    }

    @Test
    public void stream_orderby_single_field_descending_test() {
        List<JsonObject> output = jsonStream.orderBy("city", DESC).toList();
        check_list(output.stream(), "city", "NUGEGODA", "NUGEGODA",
            "GALLE", "GALLE", "GALLE", "COLOMBO",
            "COLOMBO", "COLOMBO");
    }

    @Test
    public void stream_orderby_two_field_ascending_test() {
        List<JsonObject> output = jsonStream.orderBy("city", "pop").toList();
        check_list(output.stream(), Arrays.asList("city", "pop"), "COLOMBO", 1, "COLOMBO", 2,
            "COLOMBO", 3, "GALLE", 1, "GALLE", 1, "GALLE", 2,
            "NUGEGODA", 2, "NUGEGODA", 4);
    }

    @Test
    public void stream_orderby_three_field_ascending_test() {
        List<JsonObject> output = jsonStream.orderBy("city", "pop","_id").toList();
        check_list(output.stream(), Arrays.asList("city", "pop"), "COLOMBO", 1, "COLOMBO", 2,
            "COLOMBO", 3, "GALLE", 1, "GALLE", 1, "GALLE", 2,
            "NUGEGODA", 2, "NUGEGODA", 4);
    }
    
    @Test
    public void stream_orderby_multiple_field_mix_sorting_test() {
        List<JsonObject> output = jsonStream.orderBy("city", ASC, "pop", DESC).toList();
        check_list(output.stream(), Arrays.asList("city", "pop"), "COLOMBO", 3, "COLOMBO", 2,
            "COLOMBO", 1, "GALLE", 2, "GALLE", 1, "GALLE", 1,
            "NUGEGODA", 4, "NUGEGODA", 2);
    }

    @Test
    public void stream_groupby_single_field_test() {
        List<JsonObject> output = jsonStream.groupBy("city").toList();
         check_list(output.stream(), "city", "NUGEGODA", "NUGEGODA",
            "GALLE","GALLE","GALLE","COLOMBO",
            "COLOMBO","COLOMBO");
    }

    @Test
    public void stream_groupby_multiple_field_test() {
        List<JsonObject> output = jsonStream.groupBy("city").toList();
        output.stream().forEach(System.out::println);
         check_list(output.stream(), Arrays.asList("city","pop"), "NUGEGODA",4, "NUGEGODA",2,
            "GALLE",1,"GALLE",1,"GALLE",2,"COLOMBO",1,
            "COLOMBO",3,"COLOMBO",2);
    }
    
    @Test
    public void stream_groupby_non_exist_field_test() {
        List<JsonObject> output = jsonStream.groupBy("not_exist").toList();
        output.stream().forEach(System.out::println);
        check_list(output.stream(), "city", "GALLE", "COLOMBO",
            "GALLE","NUGEGODA","GALLE","COLOMBO",
            "NUGEGODA","COLOMBO");
    }
    
    private void check_value(JsonObject json, String fieldName, Object val) {
        System.out.println("json =" + json.toString() + " field = " + fieldName + " val = " + val);
        Assert.assertTrue(JsonHelper.eq(json.get(fieldName), val));
    }

    private void check_list(Stream<JsonObject> stream, String field, Object... list) {
        List<Object> valueList = new LinkedList<>(Arrays.asList(list));
        stream.forEach(json -> check_value(json, field, valueList.remove(0)));
    }

    private void check_list(Stream<JsonObject> stream, List<String> fields, Object... list) {
        List<Object> valueList = new LinkedList<>(Arrays.asList(list));
        stream.forEach(json -> fields.stream().forEach(field -> check_value(json, field, valueList.remove(0))));
    }
}
