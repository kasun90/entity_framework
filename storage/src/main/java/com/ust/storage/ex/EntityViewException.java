package com.ust.storage.ex;

import java.text.MessageFormat;

public class EntityViewException extends Exception{

    public EntityViewException(String format,Object... args) {
        super(MessageFormat.format(format, args));
    }
    
    
}
