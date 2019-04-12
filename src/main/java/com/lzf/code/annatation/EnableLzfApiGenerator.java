package com.lzf.code.annatation;

import com.lzf.code.configuration.LzfApiConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 11:06
 *
 * @author Li Zhenfeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LzfApiConfiguration.class})
public @interface EnableLzfApiGenerator {
}
