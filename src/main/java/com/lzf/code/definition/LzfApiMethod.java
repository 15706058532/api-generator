package com.lzf.code.definition;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 16:14
 *
 * @author Li Zhenfeng
 */
public class LzfApiMethod implements Serializable, Comparable<LzfApiMethod> {
    private static final long serialVersionUID = -8172478518952372523L;
    /**
     * 请求方式 get,post,put,delete
     */
    private String http;
    /**
     * 请求URI
     */
    private String uri;
    /**
     * 功能说明
     */
    private String description;

    public LzfApiMethod(String http, String uri, String description) {
        this.http = http;
        this.uri = uri;
        this.description = description;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(LzfApiMethod lzfApiMethod) {
        return this.getUri().compareTo(lzfApiMethod.getUri());
    }
}
