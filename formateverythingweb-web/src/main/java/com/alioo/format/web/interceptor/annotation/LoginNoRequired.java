package com.alioo.format.web.interceptor.annotation;


import java.lang.annotation.*;

@Inherited
@Target( {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginNoRequired {
}
