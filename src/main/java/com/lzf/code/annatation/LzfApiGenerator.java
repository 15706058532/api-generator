package com.lzf.code.annatation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自动注入注解定义
 * Created in 2018-11-04 22:27
 *
 * @author Li Zhenfeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan({"com.lzf.code"})
public @interface LzfApiGenerator {
}
