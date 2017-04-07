/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.ex;

/**
 *
 * @author nuwan
 */
public class ServiceException extends RuntimeException{
    
    public ServiceException(String message)
    {
        super(message);
    }
    public ServiceException(Throwable thr)
    {
        super(thr);
    }
}
