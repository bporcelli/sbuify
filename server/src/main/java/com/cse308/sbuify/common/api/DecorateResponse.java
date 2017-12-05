package com.cse308.sbuify.common.api;

import com.cse308.sbuify.common.TypedCollection;

import java.lang.annotation.*;

/**
 * Controller methods marked with this annotation will have their responses decorated
 * by ResponseDecorator before serialization.
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DecorateResponse {
    Class<TypedCollection> type();
}
