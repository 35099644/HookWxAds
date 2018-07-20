package com.beichen.hookwxads.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 微信6.6.7版本对应的注解,由于注解类不能继承因此其它版本需要再生成一个类
 */
@Target(java.lang.annotation.ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Details6_6_7 {
    MetadataType type() default MetadataType.FIELD;     // 标记当前字段值是用于类,方法,字段或其它
    String ownClsName() default "";                     // 标记当前方法或字段所属的类名
    String clsName() default "";                        // 标记当前字段对应的类名
    String value() default "";                          // 设置的值,通过反射将注解值对应到字段中,名字不能更改
    String description() default "";                    // 为当前标记的字段提供描述
    String returnName() default "";                     // 当类型为方法时,标记当前方法返回值的类名
    String funArg0Name() default "";                    // 当类型为方法时,标记参数0的类名
    String funArg1Name() default "";                    // 当类型为方法时,标记参数1的类名
    String funArg2Name() default "";                    // 当类型为方法时,标记参数2的类名
    String funArg3Name() default "";                    // 当类型为方法时,标记参数3的类名
}
