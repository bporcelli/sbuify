package com.cse308.sbuify.image;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface implemented by image classes.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "image", value = Image.class),
        @JsonSubTypes.Type(name = "base64_image", value = Base64Image.class)
})
public interface ImageI {
}
