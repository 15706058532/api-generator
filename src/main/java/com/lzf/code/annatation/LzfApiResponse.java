package com.lzf.code.annatation;

import javax.validation.constraints.Null;
import java.lang.annotation.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 18:32
 *
 * @author Li Zhenfeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LzfApiResponse {
    /**
     * 对应返回类中的属性名
     *
     */
    String name();

    /**
     * 对应返回类中属性名的类
     */
    Class<?> clazz();

    /**
     * 对应类的类型 属于集合或数组 有几层写几个
     */
    ClassType[] clazzType() default {};

    /**
     * 对应返回类中属性名
     *
     */
    String pageName() default "";

    /**
     * 对应返回类中分页类中属性名的类
     *
     */
    Class<?> pageClazz() default Object.class;

    /**
     * 对应类的类型 属于集合或数组 有几层写几个
     */
    ClassType[] pageClazzType() default {};
}
