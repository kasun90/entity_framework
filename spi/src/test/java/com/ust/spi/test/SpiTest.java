/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test;

import com.ust.spi.EntityRepository;
import com.ust.spi.Injector;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.command.UserRegisterRequest;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.PasswordChanged;
import com.ust.spi.test.event.UserCreated;
import com.ust.spi.test.handler.RegisterUser;
import com.ust.spi.test.handler.ResetPassword;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD")
public class SpiTest {

  @Test
  public void user_create_test() {
    User user = new User();
    user.applyEvent(new UserCreated("nuwan", "ust123"));
    Assert.assertEquals("nuwan", user.getUsername());
    Assert.assertEquals("ust123", user.getPassword());
  }

  @Test
  public void user_password_change_test() {
    User user = new User();
    user.applyEvent(new UserCreated("nuwan", "ust123"));
    user.applyEvent(new PasswordChanged("nuwan123"));
    Assert.assertEquals("nuwan", user.getUsername());
    Assert.assertEquals("nuwan123", user.getPassword());
  }

  private User given_user(String username, String password) {
    User user = new User();
    user.applyEvent(new UserCreated(username, password));
    return user;
  }

  @Test
  public void user_create_command_test() throws Exception {
    UserRegisterRequest request = new UserRegisterRequest("nuwan", "ust123");
    EntityRepository<User> repository = new EntityRepository<>();
    RegisterUser registerUser = Injector.createInstance(RegisterUser.class, repository);
    UserResponse response = registerUser.execute(request);
    User user = repository.getEntity("nuwan");

    Assert.assertEquals(1, user.getEventsCount());
    Assert.assertEquals("", response.getError());
    Assert.assertEquals("nuwan", user.getUsername());
    Assert.assertEquals("ust123", user.getPassword());
  }

  @Test
  public void user_password_change_command_test() throws Exception {
    User user = given_user("nuwan", "ust123");
    EntityRepository<User> repository = new EntityRepository<>();
    repository.saveEntity(user);
    PasswordResetRequest request = new PasswordResetRequest("nuwan", "nuwan123");
    ResetPassword resetPassword = Injector.createInstance(ResetPassword.class, repository);
    UserResponse response = resetPassword.execute(request);

    Assert.assertEquals("", response.getError());
    Assert.assertEquals("nuwan", user.getUsername());
    Assert.assertEquals("nuwan123", user.getPassword());
  }
}
