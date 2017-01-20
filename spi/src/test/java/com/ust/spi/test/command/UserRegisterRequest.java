/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test.command;

import com.ust.spi.Command;
import com.ust.spi.test.UserResponse;

/**
 *
 * @author nuwan
 */
public class UserRegisterRequest implements Command<UserResponse>{

  private final String username;
  private final String password;

  public UserRegisterRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
 
  @Override
  public String getKey() {
    return username;
  }
  
}
