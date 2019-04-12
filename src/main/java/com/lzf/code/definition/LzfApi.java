package com.lzf.code.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 22:05
 *
 * @author Li Zhenfeng
 */
public class LzfApi {
    /**
     * 类描述
     */
    private String description;
    /**
     * 所有的接口
     */
    private List<LzfApiMethod> apiMethodList = new ArrayList<>();

    public LzfApi(String description, List<LzfApiMethod> apiMethodList) {
        this.description = description;
        this.apiMethodList = apiMethodList;
    }

    public LzfApi() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LzfApiMethod> getApiMethodList() {
        Collections.sort(apiMethodList);
        return apiMethodList;
    }

    public void setApiMethodList(List<LzfApiMethod> apiMethodList) {
        this.apiMethodList = apiMethodList;
    }
}
