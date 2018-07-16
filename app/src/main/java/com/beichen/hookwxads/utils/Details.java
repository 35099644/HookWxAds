package com.beichen.hookwxads.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface Details {
    String clsName() default "";
    String fieldName() default "";
    String funName() default "";
}
