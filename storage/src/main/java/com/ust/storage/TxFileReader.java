package com.ust.storage;

import java.util.stream.Stream;

public interface TxFileReader extends Stream<Object>{
    String get(long offset) throws Exception;
}
