package com.lzf.code.annatation;

import java.lang.annotation.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 18:32
 *
 * @author Li Zhenfeng
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LzfApiPage {
    /**
     * 对应的属性名
     *
     * @return
     */
    String name();

    /**
     * 对应的类
     *
     * @return
     */
    Class<?> clazz();

    /**
     * 对应类的类型 属于集合或数组 有几层写几个
     */
    ClassType[] clazzType() default {};
}
