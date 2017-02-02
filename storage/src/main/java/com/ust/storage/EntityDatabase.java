package com.ust.storage;

import com.ust.storage.ex.EntityCompatibilityException;
import com.ust.storage.ex.EntityViewException;
import com.ust.storage.view.EntityView;
import com.ust.storage.view.EventView;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;
import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.DBOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

public class EntityDatabase {

    static {
        RocksDB.loadLibrary();
    }

    private RocksDB db;
    private final GsonCodec gsonCodec = new GsonCodec();
    private final List<ColumnFamilyDescriptor> cfNames = new ArrayList<>();
    private final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();
    private final WriteOptions writeOptions = new WriteOptions();

    public EntityDatabase(String dbPath) throws RocksDBException {
        cfNames.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY));
        cfNames.add(new ColumnFamilyDescriptor("event_cf".getBytes()));
        DBOptions options = new DBOptions().setCreateMissingColumnFamilies(true).setCreateIfMissing(true);
        db = RocksDB.open(options, dbPath, cfNames, columnFamilyHandleList);
    }

    public void put(EntityView entityView) throws Exception {
        if (entityView.getEvents().isEmpty()) {
            return;
        }
        EntityView existingView = getEntityView(entityView.getEntityType(), entityView.getId());
        if (existingView == null) {
            if (entityView.getVersion() != 0) {
                throw new EntityViewException("invalid version {0} received for entity type {1} and id {2}",
                    entityView.getVersion(), entityView.getEntityType(), entityView.getId());
            }
            entityView.setEventSeq(0);
        } else if (existingView.getCompatibility() != entityView.getCompatibility()) {
            throw new EntityCompatibilityException("entity compatibility {0} != {1} mimatched for "
                + " entity type {2} and id {3}", existingView.getCompatibility(), entityView.getCompatibility(),
                entityView.getEntityType(), entityView.getId());
        } else if ((existingView.getVersion() + 1) != entityView.getVersion()) {
            throw new ConcurrentModificationException("version mismatched");
        }
        else
        {
            entityView.setEventSeq(existingView.getEventSeq());
        }

        List<String> events = entityView.detachEvents();
        long eventSeq = entityView.getEventSeq();
        entityView.setEventSeq(entityView.getEventSeq() + events.size());
        WriteBatch writeBatch = new WriteBatch();
        putEntityView(writeBatch, entityView);
        publishEvents(writeBatch, entityView, eventSeq, events);
        endBatch(writeBatch);
    }

    public EventResultSet queryEvents(String entityType,String entityID,long startSeq) throws UnsupportedEncodingException
    {
         RocksIterator ite = db.newIterator(columnFamilyHandleList.get(1));
         return new EventResultImpl(ite,gsonCodec, Arrays.asList(entityType,entityID,String.format("%020d",startSeq)).stream()
             .collect(Collectors.joining(":")));
    }
    
    public void close()
    {
         db.close();
    }
   
    private EntityView getEntityView(String... keys) throws RocksDBException, UnsupportedEncodingException {
        String compositeKey = Arrays.asList(keys).stream().collect(Collectors.joining(":"));
        return get(EntityView.class, compositeKey);
    }

    private void putEntityView(WriteBatch batch, EntityView entityView) throws RocksDBException, UnsupportedEncodingException {
        String compositeKey = Arrays.asList(entityView.getEntityType(), entityView.getId()).stream().collect(Collectors.joining(":"));
        put(batch, compositeKey, entityView);
    }

    private <T> T get(Class<T> clazz, String key) throws RocksDBException, UnsupportedEncodingException {
        byte[] value = db.get(key.getBytes("UTF-8"));
        if (value != null) {
            String s = new String(value, "UTF-8");
            return gsonCodec.decode(clazz, s);
        }
        return null;
    }

    private void put(WriteBatch batch, String key, Object obj) throws RocksDBException, UnsupportedEncodingException {
        byte[] value = gsonCodec.encode(obj).getBytes("UTF-8");
        batch.put(key.getBytes("UTF-8"), value);
    }

    private void publishEvents(WriteBatch batch, EntityView entityView, long startSeq, List<String> events) throws RocksDBException, UnsupportedEncodingException {
        List<EventView> list = events.stream()
            .map(event -> new EventView(event, "", System.currentTimeMillis(), System.currentTimeMillis()))
            .collect(Collectors.toList());
        long seq = startSeq;
        for (EventView eventView : list) {
            seq++;
            putEvent(batch, eventView, entityView.getEntityType(), entityView.getId(), String.format("%020d", seq));
        }
    }

    private void putEvent(WriteBatch batch, Object obj, String... keys) throws RocksDBException, UnsupportedEncodingException {
        String compositeKey = Arrays.asList(keys).stream().collect(Collectors.joining(":"));
        byte[] value = gsonCodec.encode(obj).getBytes("UTF-8");
        batch.put(columnFamilyHandleList.get(1), compositeKey.getBytes("UTF-8"), value);
    }

    private void endBatch(WriteBatch batch) throws RocksDBException {
        db.write(writeOptions, batch);
    }
    
    
}
