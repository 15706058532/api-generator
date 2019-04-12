package com.lzf.code.controller;

import com.lzf.code.context.LzfApiStorage;
import com.lzf.code.definition.LzfApi;
import com.lzf.code.definition.LzfApiParam;
import com.lzf.code.definition.LzfApiProperty;
import com.lzf.code.handler.LzfApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 20:41
 *
 * @author Li Zhenfeng
 */
@RestController
@RequestMapping("/api")
public class LzfApiController {
    @Autowired
    private LzfApiStorage lzfApiStorage;
    @Autowired
    private LzfApiHandler lzfApiHandler;

    /**
     * 查询所有的接口
     */
    @GetMapping("/all")
    public List<LzfApi> all() {
        return lzfApiStorage.getLzfApiList();
    }

    /**
     * 根据请求方式和uri查询请求参数
     */
    @GetMapping("/find/req")
    public List<LzfApiParam> findReqParamByUri(String uri) throws IOException, ClassNotFoundException {
        return lzfApiHandler.doAnalysisMethodParam(uri);
    }

    /**
     * 根据请求方式和uri查询响应参数
     */
    @GetMapping("/find/resp")
    public LzfApiParam findRespParamByUri(String uri) throws ClassNotFoundException {
        return lzfApiHandler.doAnalysisMethodReturn(uri);
    }

    /**
     * 根据请求方式和uri查询响应参数
     */
    @GetMapping("/find/properties")
    public List<LzfApiProperty> findResPropertiesByClassName(String className) throws ClassNotFoundException {
        String[] split = className.split("\\|");
        List<LzfApiProperty> lzfApiProperties = lzfApiHandler.doAnalysisProperties(split[0]);
        if (split.length > 1) {
            String[] strings = split[1].split(":");
            for (LzfApiProperty lzfApiProperty : lzfApiProperties) {
                if (Objects.equals(lzfApiProperty.getName(), strings[0])) {
                    lzfApiProperty.setClassName(strings[1]);
                }
            }
        }
        return lzfApiProperties;
    }
}
