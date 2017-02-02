package com.ust.storage;

import com.ust.storage.view.EventView;
import java.io.UnsupportedEncodingException;
import org.rocksdb.RocksIterator;

public class EventResultImpl implements EventResultSet {

    private final RocksIterator ite;
    private final String startKey;
    private final GsonCodec codec;

    public EventResultImpl(RocksIterator ite, GsonCodec codec, String startKey) throws UnsupportedEncodingException {
        this.startKey = startKey;
        this.ite = ite;
        this.codec = codec;
        this.ite.seek(startKey.getBytes("UTF-8"));
    }

    @Override
    public boolean hasNext() {
        return this.ite.isValid();
    }

    @Override
    public void next() {
        if (this.ite.isValid()) {
            this.ite.next();
        }
    }

    @Override
    public String getKey() throws UnsupportedEncodingException {
        return new String(this.ite.key(), "UTF-8");
    }

    @Override
    public EventView getValue() throws UnsupportedEncodingException {
        return codec.decode(EventView.class, new String(this.ite.value(), "UTF-8"));
    }
}
