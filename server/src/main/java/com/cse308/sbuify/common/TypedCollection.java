package com.cse308.sbuify.common;

import com.cse308.sbuify.common.api.Decorable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Wraps a collection to ensure that type information is included when its elements are serialized.
 *
 * Example usage:
 *
 * public ResponseEntity<?> myControllerMethod() {
 *     ...
 *     Set<Artist> artists = ...;
 *     return new ResponseEntity<>(new TypedCollection(artists, Artist.class), HttpStatus.OK);
 * }
 */
public class TypedCollection implements JsonSerializable, Decorable {

    /** Wrapped collection */
    private Collection collection;

    /** Class type of elements */
    private Class<?> elementType;

    public TypedCollection(Collection collection, Class<?> elementType) {
        this.collection = collection;
        this.elementType = elementType;
    }

    public Class<?> getElementType() {
        return elementType;
    }

    public Collection getCollection() {
        return collection;
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeRawValue("[");
        JsonSerializer<Object> serializer = serializerProvider
                .findTypedValueSerializer(elementType, false, null);
        Iterator<Object> objectIterator = collection.iterator();
        while (objectIterator.hasNext()) {
            Object o = objectIterator.next();
            serializer.serialize(o, jsonGenerator, serializerProvider);
            if (objectIterator.hasNext()) {  // add delimiting comma
                jsonGenerator.writeRawValue(",");
            }
        }
        jsonGenerator.writeRawValue("]");
    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
        serialize(jsonGenerator, serializerProvider);
    }
}
