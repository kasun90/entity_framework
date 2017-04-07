/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi;

/**
 *
 * @author nuwansa
 */
public class QueryRequest {

    private final String query;

    public QueryRequest(String query) {
        this.query = query;
    }

    public String query() {
        return this.query;
    }
}
