package com.ust.storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TxFileWriterImpl implements TxFileWriter {

    String fileName;
    FileOutputStream outputStream;
    
    public static final int RS = 30;
    public static final int US = 31;
    
    public TxFileWriterImpl(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
    }

    @Override
    public void init() throws FileNotFoundException {
        outputStream = new FileOutputStream(fileName, true);
    }

    @Override
    public void add(String msg) throws Exception {
        outputStream.write(msg.getBytes("UTF-8"));
    }

    @Override
    public void add(String... msgs) throws Exception {
        String tx = Arrays.asList(msgs).stream().collect(Collectors.joining(""+(char)0x1f));
        add(tx);
    }
    
    @Override
    public String getFilename() {
        return fileName;
    }
    
    @Override
    public void endTransaction() throws Exception {
        outputStream.write(RS);
        outputStream.flush();
    }

    @Override
    public long getLen() throws IOException {
        return outputStream.getChannel().size();
    }
    
     @Override
    public void close() {
        try {
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(TxFileWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void recover() throws Exception {
        int readBlockSize = 1024 * 1024;
        byte[] data = new byte[readBlockSize];
        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
            long length = file.getChannel().size();
            int truncateLen = 0;
            while (length > 0) {
                int readLen = Integer.min((int) length, readBlockSize);
                file.seek(length - readLen);
                readLen = file.read(data, 0, readLen);
                int offset = searchRS(data, readLen);
                if (offset == -1) {
                    truncateLen += readLen;
                    length -= readLen;
                } else {
                    truncateLen += (readLen - offset);
                    break;
                }
            }
            file.getChannel().truncate(file.getChannel().size() - truncateLen);
        }
    }

    private int searchRS(byte[] data, int length) {
        while (length > 0) {
            int b = data[--length];
            if (b == 30) {
                return length + 1;
            }
        }
        return -1;
    }

}
