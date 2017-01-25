package com.ust.spi;

import com.ust.spi.ex.CommandException;

import java.lang.reflect.Field;

/**
 * Utility class for the test case {@link EntityCommandHandler} creation.
 */
public final class Injector {
    private Injector(){

    }

    /**
     * This creates an instance of a {@link EntityCommandHandler} injected the {@link EntityRepository} as it
     * repository.
     * @param cls the {@link EntityCommandHandler} type for creating new instance.
     * @param repo  the repository to be injected to the {@link EntityCommandHandler}.
     * @param <T> the {@link EntityCommandHandler} type for creating new instance.
     * @return the created {@link EntityCommandHandler}
     */
    public static <T extends EntityCommandHandler> T createInstance(Class<T> cls, EntityRepository repo) {
        try {
            Field field = cls.getSuperclass().getDeclaredField("entityRepo");
            field.setAccessible(true);
            T handler = cls.newInstance();
            field.set(handler, repo);
            return handler;
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            throw new CommandException(e);
        }
    }
}
