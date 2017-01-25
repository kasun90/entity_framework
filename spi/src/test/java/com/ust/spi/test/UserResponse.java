/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test;

/**
 * @author nuwan
 */
public class UserResponse {

    private final String error;

    public UserResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
