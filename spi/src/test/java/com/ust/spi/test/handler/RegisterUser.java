/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test.handler;

import com.ust.spi.EntityCommandHandler;
import com.ust.spi.test.UserResponse;
import com.ust.spi.test.event.UserCreated;
import com.ust.spi.test.command.UserRegisterRequest;
import com.ust.spi.test.entity.User;

/**
 *
 * @author nuwan
 */
public class RegisterUser extends EntityCommandHandler<UserRegisterRequest, UserResponse, User> {

  @Override
  public UserResponse execute(UserRegisterRequest cmd) throws Exception {

    User user = getRepository().getEntity(cmd.getUsername());
    if (user != null) {
      return new UserResponse("user already exist");
    }
    user = new User();
    user.applyEvent(new UserCreated(cmd.getUsername(), cmd.getPassword()));
    getRepository().saveEntity(user);
    return new UserResponse("");
  }

}
