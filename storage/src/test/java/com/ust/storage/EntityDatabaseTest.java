package com.ust.storage;

import com.ust.storage.ex.EntityCompatibilityException;
import com.ust.storage.view.EntityView;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.rocksdb.RocksDBException;

@SuppressWarnings("PMD")
public class EntityDatabaseTest {

    EntityDatabase db;

    public EntityDatabaseTest() {
    }

    @Rule
    public TemporaryFolder dbFolder = new TemporaryFolder();

    @Before
    public void setUp() throws RocksDBException {
        db = new EntityDatabase(dbFolder.getRoot().getAbsolutePath() + "/rockdb");
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void first_entity_view_insert_test() throws Exception {

        EntityView entityView = new EntityView(0, 0, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event1", "event2", "event3")));
        db.put(entityView);

        EventResultSet result = db.queryEvents("com.ust.User", "1", 1);
        int i = 0;
        while (result.hasNext()) {
            i++;
            assertEquals("com.ust.User:1:0000000000000000000" + i, result.getKey());
            assertEquals("event" + i, result.getValue().getEvent());
            System.out.println("key " + result.getKey() + " val " + result.getValue());
            result.next();
        }
    }

    @Test
    public void entity_view_continues_write_event_sequence_test() throws Exception {

        EntityView entityView = new EntityView(0, 0, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event1", "event2", "event3")));
        db.put(entityView);

        entityView = new EntityView(1, 0, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event4", "event5", "event6")));
        db.put(entityView);

        EventResultSet result = db.queryEvents("com.ust.User", "1", 1);
        int i = 0;
        while (result.hasNext()) {
            i++;
            assertEquals("com.ust.User:1:0000000000000000000" + i, result.getKey());
            assertEquals("event" + i, result.getValue().getEvent());
            System.out.println("key " + result.getKey() + " val " + result.getValue());
            result.next();
        }
        assertEquals(6, i);
    }

    @Test
    public void entity_view_concurrent_modification_test() throws Exception {

        //use same version for two writes
        int version = 0;
        EntityView entityView = new EntityView(version, 0, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event1", "event2", "event3")));
        db.put(entityView);

        entityView = new EntityView(version, 0, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event4", "event5", "event6")));
        try {
            db.put(entityView);
        } catch (ConcurrentModificationException ex) {
            assertEquals("version mismatched", ex.getMessage());
            return;
        }
        Assert.fail("this test should throw a exception");
    }

    @Test
    public void entity_view_compatibility_test() throws Exception {
        //compatilibility version start with 0
        int compatibility = 0;
        EntityView entityView = new EntityView(0, compatibility, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event1", "event2", "event3")));
        db.put(entityView);

        //compatilibility version change to 1
        compatibility = 1;
        entityView = new EntityView(1, compatibility, "com.ust.User", "1", "entity1",
            new LinkedList<>(Arrays.asList("event4", "event5", "event6")));
        try {
            db.put(entityView);
        } catch (Exception ex) {
            assertEquals(ex.getClass(), EntityCompatibilityException.class);
            return;
        }
        Assert.fail("this test should throw a exception");
    }
}
