package com.ust.storage.ex;

import java.text.MessageFormat;

public class EntityCompatibilityException extends Exception{

    public EntityCompatibilityException(String format,Object... args) {
        super(MessageFormat.format(format, args));
    }
    
    
}
