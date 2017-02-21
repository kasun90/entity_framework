/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test;

import com.ust.spi.EntityCommandHandler;
import com.ust.spi.EntityRepository;
import com.ust.spi.Event;
import com.ust.spi.Injector;
import com.ust.spi.MutableCache;
import com.ust.spi.ex.CommandException;
import com.ust.spi.ex.EntityException;
import com.ust.spi.ex.EventException;
import com.ust.spi.test.command.PasswordResetRequest;
import com.ust.spi.test.command.UserRegisterRequest;
import com.ust.spi.test.entity.TestApplyExceptionMapEntity;
import com.ust.spi.test.entity.TestItemCreateExceptionMapEntity;
import com.ust.spi.test.entity.TestMapEntity;
import com.ust.spi.test.entity.User;
import com.ust.spi.test.event.EntityExceptionCreationEvent;
import com.ust.spi.test.event.PasswordChanged;
import com.ust.spi.test.event.UnusedEvent;
import com.ust.spi.test.event.UserCreated;
import com.ust.spi.test.handler.CommandExceptionHandler;
import com.ust.spi.test.handler.RegisterUser;
import com.ust.spi.test.handler.ResetPassword;
import com.ust.spi.test.handler.TestEventCreateExceptionHandler;
import com.ust.spi.test.handler.TestEventHandler;
import com.ust.spi.test.handler.TestEventProcessingExceptionHandler;
import com.ust.spi.test.handler.TestExceptionHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

@SuppressWarnings("PMD")
public class SpiTest {

    Injector injector;

    @Before
    public void before() {
        injector = new InMemoryInjector();
    }

    @Test(expected = EntityException.class)
    public void entity_exception_test() {
        User user = new User();
        EntityExceptionCreationEvent event = new EntityExceptionCreationEvent();
        user.applyEvent(event);
    }

    @Test(expected = CommandException.class)
    public void command_exception_test() {
        CommandExceptionHandler handler = new CommandExceptionHandler();
        handler.execute(new PasswordResetRequest("", ""));
    }

    @Test
    public void coverInjector() throws Exception {
        Injector injector = new InMemoryInjector();
        Assert.assertNotNull(injector.getRepositoryRegistry());
        Assert.assertNotNull(injector.getCacheRegistry());
    }

    @Test
    public void coverInjectorExceptions() throws Exception {
        Injector injector = new InMemoryInjector();
        Assert.assertNotNull(injector.getRepositoryRegistry());
        Assert.assertNotNull(injector.getCacheRegistry());
        try {
            injector.createInstance(TestExceptionHandler.class);
            Assert.fail();
        } catch (Exception ignored) {

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
        Assert.assertEquals(1, mapEntity.stream().collect(Collectors.toList()).size());
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

    @Test(expected = EntityException.class)
    public void mapEntityApplyExceptionEventTest() {
        TestApplyExceptionMapEntity mapEntity = new TestApplyExceptionMapEntity();
        Event event = new PasswordChanged("");
        mapEntity.applyEvent("1", event);
        Assert.assertEquals(0, mapEntity.getItems().size());
    }

    @Test(expected = EntityException.class)
    public void mapEntityItemCreateExceptionEventTest() {
        TestItemCreateExceptionMapEntity mapEntity = new TestItemCreateExceptionMapEntity();
        mapEntity.applyEvent("1", new PasswordChanged(""));
        Assert.assertEquals(0, mapEntity.getItems().size());
    }

    @Test(expected = EntityException.class)
    public void mapEntityInvalidEventTest() {
        TestMapEntity mapEntity = new TestMapEntity();
        mapEntity.applyEvent("1", new UnusedEvent());
    }

    @Test(expected = EntityException.class)
    public void entity_exception_test2() {
        User user = new User();
        user.applyEvent(new UnusedEvent());
    }

    @Test
    public void user_create_test() {
        User user = new User();
        UserCreated event = new UserCreated("nuwan", "ust123");
        user.applyEvent(event);
        Assert.assertEquals("nuwan", user.getUsername());
        Assert.assertEquals("ust123", user.getPassword());
        Assert.assertEquals(1, user.getEvents().size());
        Assert.assertNotEquals(0L, event.getTime());
    }

    @Test
    public void user_password_change_test() {
        User user = new User();
        user.applyEvent(new UserCreated("nuwan", "ust123"));
        user.applyEvent(new PasswordChanged("nuwan123"));
        Assert.assertEquals("nuwan", user.getUsername());
        Assert.assertEquals("nuwan123", user.getPassword());
    }

    @Test
    public void viewRepositoryGetTest() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest("nuwan", "ust123");
        RegisterUser registerUser = injector.createInstance(RegisterUser.class);
        UserResponse response = registerUser.execute(request);
        User user = injector.getRepositoryRegistry().getRepository(User.class).getEntity("nuwan");

        Assert.assertEquals(1, user.getEventsCount());
        Assert.assertEquals("nuwan", response.getError());
        Assert.assertEquals("nuwan", user.getUsername());
        Assert.assertEquals("ust123", user.getPassword());
    }

    @Test
    public void user_create_command_test() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest("nuwan", "ust123");
        RegisterUser registerUser = injector.createInstance(RegisterUser.class);
        UserResponse response = registerUser.execute(request);
        User user = injector.getRepositoryRegistry().getRepository(User.class).getEntity("nuwan");

        Assert.assertEquals(1, user.getEventsCount());
        Assert.assertEquals("nuwan", response.getError());
        Assert.assertEquals("nuwan", user.getUsername());
        Assert.assertEquals("ust123", user.getPassword());
    }

    @Test
    public void user_password_change_command_test() throws Exception {
        User user = given_user("nuwan", "ust123");
        EntityRepository<User> repository = injector.getRepositoryRegistry().getRepository(User.class);
        repository.saveEntity(user);
        PasswordResetRequest request = new PasswordResetRequest("nuwan", "nuwan123");
        ResetPassword resetPassword = injector.createInstance(ResetPassword.class);
        UserResponse response = resetPassword.execute(request);

        Assert.assertEquals("", response.getError());
        Assert.assertEquals("nuwan", user.getUsername());
        Assert.assertEquals("nuwan123", user.getPassword());
    }

    private User given_user(String username, String password) {
        User user = new User();
        user.applyEvent(new UserCreated(username, password));
        return user;
    }

    @Test
    public void ownCacheTest() throws Exception {
        InMemoryCacheRegistry reg = new InMemoryCacheRegistry();

        EntityCommandHandler<UserRegisterRequest, UserResponse, User> resetPassword
                = injector.createInstance(ownCacheTestClass1.class);
        resetPassword.execute(new UserRegisterRequest("test", "best"));
        resetPassword = injector.createInstance(ownCacheTestClass2.class);
        resetPassword.execute(new UserRegisterRequest("test", "best"));
    }

    @Test
    public void eventHandlerTest() {
        InMemoryInjector injector = new InMemoryInjector();
        TestEventHandler listenerInstance = injector.createListenerInstance(TestEventHandler.class);
        listenerInstance.onEvent(new UserCreated("simple", "dimple"));
        Assert.assertEquals("simple", listenerInstance.getEvent().getUsername());
        Assert.assertEquals("dimple", listenerInstance.getEvent().getPassword());
    }

    @Test(expected = EventException.class)
    public void eventCreateExceptionHandlerTest() {
        InMemoryInjector injector = new InMemoryInjector();
        TestEventCreateExceptionHandler listenerInstance
                = injector.createListenerInstance(TestEventCreateExceptionHandler.class);
    }

    @Test(expected = EventException.class)
    public void eventProcessExceptionHandlerTest() {
        InMemoryInjector injector = new InMemoryInjector();
        TestEventProcessingExceptionHandler listenerInstance
                = injector.createListenerInstance(TestEventProcessingExceptionHandler.class);
        listenerInstance.onEvent(new UserCreated("simple", "dimple"));
    }

    public static class ownCacheTestClass1 extends EntityCommandHandler<UserRegisterRequest, UserResponse, User> {

        @Override
        public UserResponse execute(UserRegisterRequest cmd) {
            MutableCache cache = getCache(cmd.getUsername());
            cache.put("test", "Go");

            ((MutableCache) getCache(User.class)).put("lot", "BOT");
            return null;
        }
    }

    public static class ownCacheTestClass2 extends EntityCommandHandler<UserRegisterRequest, UserResponse, User> {

        @Override
        public UserResponse execute(UserRegisterRequest cmd) {
            Assert.assertEquals("Go", getCache(cmd.getUsername()).get("test"));

            Assert.assertEquals("Go", getCache(User.class, cmd.getUsername()).get("test"));
            Assert.assertEquals("BOT", getCache(User.class).get("lot"));
            return null;
        }
    }
}
