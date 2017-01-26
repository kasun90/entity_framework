package com.ust.spi;

import com.ust.spi.annotation.Inject;

public abstract class EntityEventHandler<E extends Event, I extends Entity> implements EventHandler<E> {

  @Inject
  private EntityRepository<I> entityRepo;

  @Override
  public abstract void onEvent(E event);

  protected EntityRepository<I> getRepository() {
    return entityRepo;
  }
}
