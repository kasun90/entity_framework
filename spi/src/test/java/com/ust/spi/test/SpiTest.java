/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test;

import com.ust.spi.EntityRepository;
import com.ust.spi.Injector;
import com.ust.spi.ex.CommandException;
import com.ust.spi.ex.EntityException;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.command.UserRegisterRequest;
import com.ust.spi.test.entity.Instrument;
import com.ust.spi.test.entity.TestMapEntity;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.EntityExceptionCreationEvent;
import com.ust.spi.test.event.PasswordChanged;
import com.ust.spi.test.event.UnusedEvent;
import com.ust.spi.test.event.UserCreated;
import com.ust.spi.test.handler.CommandExceptionHandler;
import com.ust.spi.test.handler.RegisterUser;
import com.ust.spi.test.handler.ResetPassword;
import com.ust.spi.test.handler.TestHandler;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

@SuppressWarnings("PMD")
public class SpiTest {

    @Test
    public void entity_exception_test() {
        //Injector injector = new Injector();
        User user = new User();
        EntityExceptionCreationEvent event = new EntityExceptionCreationEvent();
        try {
            user.applyEvent(event);
            Assert.fail();
        } catch (Throwable ex) {
            Assert.assertEquals(EntityException.class, ex.getClass());
        }
        Assert.assertEquals(null, event.getEntityId());
    }

    @Test
    public void command_exception_test() {
        CommandExceptionHandler handler = new CommandExceptionHandler();
        try {
            handler.execute(new PasswordResetRequest("", ""));
            Assert.fail();
        } catch (Throwable ex) {
            Assert.assertEquals(CommandException.class, ex.getClass());
        }
    }

    @Test
    public void coverInjector() throws Exception {
        Constructor<Injector> constructor = Injector.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
        try {
            Injector.createInstance(TestHandler.class, new EntityRepository<Instrument>());
            Assert.fail();
        } catch (Throwable ignored) {

        }
    }

    @Test
    public void mapEntityEventTest() {
        TestMapEntity mapEntity = new TestMapEntity();
        mapEntity.applyEvent(new UserCreated("", ""));
        Assert.assertEquals(0, mapEntity.getItems().size());
    }

    @Test
    public void mapEntityItemEventTest() throws Exception {
        TestMapEntity mapEntity = new TestMapEntity();
        mapEntity.applyEvent("1", new PasswordChanged(""));
        Assert.assertEquals(1, mapEntity.getItems().size());
    }

    @Test
    public void mapEntityExceptionTest() {
        TestMapEntity mapEntity = new TestMapEntity();
        try {
            mapEntity.applyEvent("1", new EntityExceptionCreationEvent());
            Assert.fail();
        } catch (Throwable ex) {
            Assert.assertEquals(EntityException.class, ex.getClass());
        }
    }

    @Test
    public void mapEntityInvalidEventTest() {
        TestMapEntity mapEntity = new TestMapEntity();
        try {
            mapEntity.applyEvent("1", new UnusedEvent());
            Assert.fail();
        } catch (Throwable ex) {
            Assert.assertEquals(EntityException.class, ex.getClass());
        }
    }

    @Test
    public void entity_exception_test2() {
        User user = new User();
        try {
            user.applyEvent(new UnusedEvent());
            Assert.fail();
        } catch (Throwable ex) {
            Assert.assertEquals(EntityException.class, ex.getClass());
        }
        Assert.assertEquals(0, user.getEvents().size());
    }

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
