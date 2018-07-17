package com.beichen.hookwxads.utils;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Details6_6_7 {
    MetadataType type() default MetadataType.FIELD;     // 标记当前字段值是用于类,方法,字段或其它
    String ownClsName() default "";                     // 标记当前方法或字段所属的类名
    String clsName() default "";                        // 标记当前字段对应的类名
    String value() default "";                          // 设置的值,通过反射将注解值对应到字段中,名字不能更改
    String description() default "";                    // 为当前标记的字段提供描述
}
