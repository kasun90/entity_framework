/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test.handler;

import com.ust.spi.EntityCommandHandler;
import com.ust.spi.test.UserResponse;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.PasswordChanged;

/**
 *
 * @author nuwan
 */
public class ResetPassword extends EntityCommandHandler<PasswordResetRequest, UserResponse, User> {

  @Override
  public UserResponse execute(PasswordResetRequest cmd) throws Exception {
    User user = getRepository().getEntity(cmd.getUsername());
    if (user == null) {
      return new UserResponse("user not found");
    }
    user.applyEvent(new PasswordChanged(cmd.getPassword()));
    return new UserResponse("");
  }

}
