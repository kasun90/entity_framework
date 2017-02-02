package com.ust.storage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("PMD")
public class TxFileWriterImplTest {

    private String given_file() throws IOException {
        File temp = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
        temp.deleteOnExit();
        return temp.getAbsolutePath();
    }

    @Test
    public void test_file_partial_write_recovery() throws Exception {
        TxFileWriterImpl writer = new TxFileWriterImpl(given_file());
        writer.init();
        writer.add("msg1");
        writer.endTransaction();
        writer.add("msg2");
        writer.close();
        writer = new TxFileWriterImpl(writer.getFilename());
        writer.recover();
        writer.init();
        assertEquals(5, writer.getLen());
        writer.close();
    }

    @Test
    public void test_file_full_reset() throws Exception {
        TxFileWriterImpl writer = new TxFileWriterImpl(given_file());
        writer.init();
        writer.add("msg1");
        writer.close();
        writer = new TxFileWriterImpl(writer.getFilename());
        writer.recover();
        writer.init();
        assertEquals(0, writer.getLen());
        writer.close();
    }

    @Test
    public void test_file_complete_write() throws Exception {
        TxFileWriterImpl writer = new TxFileWriterImpl(given_file());
        writer.init();
        writer.add("msg1");
        writer.endTransaction();
        writer.add("msg2");
        writer.endTransaction();
        writer.close();
        writer = new TxFileWriterImpl(writer.getFilename());
        writer.recover();
        writer.init();
        assertEquals(10, writer.getLen());
        writer.close();
    }

    @Test
    public void test_file_transaction_test() throws Exception {
        TxFileWriterImpl writer = new TxFileWriterImpl(given_file());
        writer.init();
        writer.add("msg1","msg2","msg3");
        writer.endTransaction();
        writer.close();
        writer = new TxFileWriterImpl(writer.getFilename());
        writer.recover();
        writer.init();
        assertEquals(15, writer.getLen());
        writer.close();
    }
    
    @Test
    public void test_file_transaction_rollback() throws Exception {
        TxFileWriterImpl writer = new TxFileWriterImpl(given_file());
        writer.init();
        writer.add("msg1");
        writer.endTransaction();
        writer.add("msg2","msg3","msg4");
        writer.close();
        writer = new TxFileWriterImpl(writer.getFilename());
        writer.recover();
        writer.init();
        assertEquals(5, writer.getLen());
        writer.close();
    }

}
