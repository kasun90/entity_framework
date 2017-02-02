package com.ust.storage;

import com.ust.storage.view.EventView;
import java.io.UnsupportedEncodingException;

public interface EventResultSet {

    boolean hasNext()throws UnsupportedEncodingException;

    void next()throws UnsupportedEncodingException;

    String getKey() throws UnsupportedEncodingException;

    EventView getValue() throws UnsupportedEncodingException;
}
