package com.ust.storage.query;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import static com.ust.storage.query.JsonHelper.eq;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonStream {

    public static final Direction ASC = Direction.ASC;
    public static final Direction DESC = Direction.DESC;

    private Stream<JsonObject> stream;

    public JsonStream(Iterator<JsonObject> jsonIterator) {
        Iterable<JsonObject> iterable = () -> jsonIterator;
        stream = StreamSupport.stream(iterable.spliterator(), false);
    }

    public JsonStream(Stream<JsonObject> stream) {
        this.stream = stream;
    }

    public JsonStream filter(String field, Object value) {
        stream = stream.filter(json -> eq(json.get(field), value));
        return this;
    }

    public JsonStream groupBy(String... fields) {
        Map<String, List<JsonObject>> map = stream.collect(Collectors.groupingBy(json -> keys(json, fields)));
        stream = map.entrySet().stream().flatMap(set -> set.getValue().stream());
        return this;
    }

    public JsonStream orderBy(Object... fields) {
        stream = stream.sorted(comparator(fields));
        return this;
    }

    public JsonStream limit(int count) {
        stream = stream.limit(count);
        return this;
    }

    public List<JsonObject> toList() {
        return stream.collect(Collectors.toList());
    }

    public static String keys(JsonObject obj, String... keys) {
        return new Keys(obj, keys).toString();
    }

    public static final class Keys {

        JsonObject json;
        String[] keys;

        public Keys(JsonObject json, String... keys) {
            this.json = json;
            this.keys = keys;
        }

        public String toString() {
            return Arrays.asList(keys).stream().map(key -> getValue(key)).collect(Collectors.joining(":"));
        }

        private String getValue(String key) {
            JsonElement el = json.get(key);
            if (el == null) {
                return "<null>";
            } else {
                return el.getAsString();
            }
        }
    }

    public static final Comparator<JsonObject> comparator(Object... fields) {
        int skip = 0;
        Comparator<JsonObject> head = new JsonFieldComparator((String) fields[0]);
        skip++;
        if (fields.length > 1 && fields[1] instanceof Direction) {
            head = ((JsonFieldComparator) head).direction((Direction) fields[1]);
            skip++;
        }
        List<Object> rest = Arrays.asList(fields).stream().skip(skip).collect(Collectors.toList());

        Comparator<JsonObject> current = null;
        for (Object field : rest) {
            if (current == null && field instanceof String) {
                current = new JsonFieldComparator((String) field);
            } else if (current != null && field instanceof Direction) {
                current = ((JsonFieldComparator) current).direction((Direction) field);
                head = head.thenComparing(current);
                current = null;
            } else if (current != null && field instanceof String) {
                head = head.thenComparing(current);
                current = new JsonFieldComparator((String) field);
            }

        }
        if (current != null) {
            head = head.thenComparing(current);
        }

        return head;
    }

    public static final class JsonFieldComparator implements Comparator<JsonObject> {

        String field;

        public JsonFieldComparator(String field) {
            this.field = field;
        }

        public Comparator<JsonObject> direction(Direction direction) {
            if (direction == Direction.DESC) {
                return this.reversed();
            } else {
                return this;
            }
        }

        @Override
        public int compare(JsonObject left, JsonObject right) {
            return JsonHelper.compare(left.get(field), right.get(field));
        }

    }

}
