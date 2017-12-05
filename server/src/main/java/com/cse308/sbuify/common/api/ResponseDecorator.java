package com.cse308.sbuify.common.api;

public interface ResponseDecorator<T> {  // fixme: should be T extends Decorable

    /** Does the decorator support the given class type? */
    boolean supports(Class<?> type);

    /** Decorate an object */
    void decorate(T original);
}
