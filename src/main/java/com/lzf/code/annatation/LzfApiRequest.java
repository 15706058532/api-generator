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
     * 必须字段数组   *表示全部
     *
     * @return
     */
    String[] musts() default {};

    /**
     * 非必须字段数组 *表示全部
     *
     * @return
     */
    String[] noMusts() default {};
}
