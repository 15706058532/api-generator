package com.lzf.code.annatation;

import java.lang.annotation.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 18:25
 *
 * @author Li Zhenfeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LzfApiRequest {
    /**
     *  请求类
     */
    Class[] reqClass();

    String[] mustProperty() default {};
}
