package com.ust.spi;

import com.ust.spi.ex.CommandException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class for the test case {@link EntityCommandHandler} creation.
 */
public final class Injector {
    private Injector() {

    }

    /**
     * This creates an instance of a {@link EntityCommandHandler} injected the {@link EntityRepository} as it
     * repository.
     *
     * @param cls the {@link EntityCommandHandler} type for creating new instance.
     * @param <T> the {@link EntityCommandHandler} type for creating new instance.
     * @return the created {@link EntityCommandHandler}
     */
    @SuppressWarnings("unchecked")
    public static <T extends EntityCommandHandler> T createInstance(Class<T> cls) {
        try {
            Field field = cls.getSuperclass().getDeclaredField("entityRepo");
            field.setAccessible(true);
            T handler = cls.newInstance();
            Type genericFieldType = handler.getClass().getGenericSuperclass();
            ParameterizedType type = (ParameterizedType) genericFieldType;
            Class entityTypeClass = (Class) type.getActualTypeArguments()[2];
            field.set(handler, RepositoryRegistry.getInstance().getRepository(entityTypeClass));
            return handler;
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            throw new CommandException(e);
        }


    }
}
