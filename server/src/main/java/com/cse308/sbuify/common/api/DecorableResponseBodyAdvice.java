package com.cse308.sbuify.common.api;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

@ControllerAdvice
public class DecorableResponseBodyAdvice implements ResponseBodyAdvice<Decorable> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return findAnnotation(methodParameter) != null;
    }

    @Nullable
    @Override
    public Decorable beforeBodyWrite(@Nullable Decorable decorable, MethodParameter methodParameter, MediaType mediaType,
                                     Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                     ServerHttpResponse serverHttpResponse) {
        DecorateResponse annotation = findAnnotation(methodParameter);
        ResponseDecorator<?> decorator = DecoratorRegistry.getDecorator(annotation.type());
        if (decorator == null) {
            throw new DecoratorException("There are no registered decorators for type " + decorator);
        }
        return decorateWith(decorable, decorator);
    }

    private <T> Decorable decorateWith(Decorable decorable, ResponseDecorator<T> responseDecorator) {
        responseDecorator.decorate((T) decorable);
        return decorable;
    }

    private DecorateResponse findAnnotation(MethodParameter methodParameter) {
        Annotation[] annotations = methodParameter.getMethodAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].annotationType().equals(DecorateResponse.class)) {
                return (DecorateResponse) annotations[i];
            }
        }
        return null;
    }
}
