package com.lzf.code.definition;

import java.io.Serializable;
import java.util.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 16:19
 *
 * @author Li Zhenfeng
 */
public class LzfApiParam implements Serializable {
    private static final long serialVersionUID = 7092778819782108650L;
    /**
     * 数据格式 json或formData
     */
    private String format;
    /**
     * 在返回值解析时使用 ，为对象时返回object
     */
    private String type;

    /**
     * 属性
     */

    private Collection<LzfApiProperty> lzfApiProperties = new ArrayList<>();

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Collection<LzfApiProperty> getLzfApiProperties() {
        return lzfApiProperties;
    }

    public void setLzfApiProperties(Collection<LzfApiProperty> lzfApiProperties) {
        this.lzfApiProperties = lzfApiProperties;
    }

    public void addLzfApiPropertie(LzfApiProperty lzfApiPropertie) {
        this.lzfApiProperties.add(lzfApiPropertie);
    }

    public void addLzfApiPropertiesAll(Collection<LzfApiProperty> lzfApiProperties) {
        this.lzfApiProperties.addAll(lzfApiProperties);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
