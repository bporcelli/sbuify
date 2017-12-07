package com.cse308.sbuify.common.api;

// todo: (brett) if time allows, add ?extends Decorable qualifier to type parameter
public interface ResponseDecorator<T> {

    /** Does the decorator support the given class type? */
    boolean supports(Class<?> type);

    /** Decorate an object */
    void decorate(T original);
}
