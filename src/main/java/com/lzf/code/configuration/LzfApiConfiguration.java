package com.lzf.code.configuration;

import com.lzf.code.context.LzfApiContext;
import com.lzf.code.handler.LzfApiHandler;
import com.lzf.code.context.LzfApiStorage;
import com.lzf.code.controller.LzfApiController;
import com.lzf.code.controller.LzfApiIndexController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 11:07
 *
 * @author Li Zhenfeng
 */
@Configuration
public class LzfApiConfiguration {
    @Bean
    public LzfApiIndexController getApiController() {
        return new LzfApiIndexController();
    }

    @Bean
    public LzfApiController getLzfApiController() {
        return new LzfApiController();
    }

    @Bean
    public LzfApiContext getLzfApiContext() {
        return new LzfApiContext();
    }

    @Bean
    public LzfApiHandler getLzfApiHandler() {
        return new LzfApiHandler();
    }

    @Bean
    public LzfApiStorage getLzfApiStorage() {
        return new LzfApiStorage();
    }
}
