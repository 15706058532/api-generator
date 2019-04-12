package com.lzf.code.annatation;

import java.lang.annotation.*;

/**
 * 自动注入注解定义
 * Created in 2018-11-04 22:27
 *
 * @author Li Zhenfeng
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LzfApiDescribe {
    String value() default "";

    boolean must() default false;
}
