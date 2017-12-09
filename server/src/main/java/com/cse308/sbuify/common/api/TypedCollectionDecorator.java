package com.cse308.sbuify.common.api;

import com.cse308.sbuify.common.TypedCollection;

import java.util.Collection;

public class TypedCollectionDecorator implements ResponseDecorator<TypedCollection> {

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(TypedCollection.class);
    }

    @Override
    public void decorate(TypedCollection original) {
        decorate(original, original.getElementType());
    }

    private <T> void decorate(TypedCollection original, Class<T> elementType) {
        ResponseDecorator<T> decorator = DecoratorRegistry.getDecorator(elementType);
        if (decorator == null) {
            return;
        }
        for (Object obj: (Collection) original.getCollection()) {  // fixme: add type param to collection
            decorator.decorate((T) obj);
        }
    }
}
