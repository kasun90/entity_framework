/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.impl;

import com.ust.spi.Command;
import com.ust.spi.EntityCommandHandler;
import com.ust.spi.Injector;

/**
 *
 * @author nuwansa
 */
public interface MethodExecutor {
        Object execute(Object reqeust);
}

class CommandExecutor implements MethodExecutor
{
    Class<? extends EntityCommandHandler> commandHandlerClazz;
    EntityCommandHandler cmdHandler;

    public CommandExecutor(Injector injector) {
        cmdHandler = injector.createInstance(commandHandlerClazz);
    }
    
    
    @Override
    public Object execute(Object reqeust) {
        return cmdHandler.execute(Command.class.cast(reqeust));
    }
    
}