package com.ust.storage;

import java.util.Collection;

public interface TxFileWriter {

    void init() throws Exception;;

    void add(String msg) throws Exception;

    void add(String... msg) throws Exception;
    
    void endTransaction() throws Exception;
    
    void recover() throws Exception;
    
    void close();
    
    String getFilename();
    
    long getLen() throws Exception;
}
