package com.lzf.code.definition;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 16:56
 *
 * @author Li Zhenfeng
 */
public class LzfApiProperty implements Serializable {
    private static final long serialVersionUID = 2030311496888129440L;
    /**
     * 值名称
     */
    private String name;
    /**
     * 值类型
     */
    private String type;
    /**
     * 值是否必须
     */
    private Boolean isMust = false;
    /**
     * 值描述
     */
    private String describe;

    /**
     * 如果是类类型或者集合类型，则保存类的全名
     */
    private String className;


    private Boolean isError = false;

    private String errorMsg;

    public LzfApiProperty() {
    }

    public LzfApiProperty(String name, String type, Boolean isMust, String describe) {
        this.name = name;
        this.type = type;
        this.isMust = isMust;
        this.describe = describe;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getMust() {
        return isMust;
    }

    public void setMust(Boolean must) {
        isMust = must;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "LzfApiProperty{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isMust=" + isMust +
                ", describe='" + describe + '\'' +
                ", className='" + className + '\'' +
                ", isError=" + isError +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LzfApiProperty that = (LzfApiProperty) o;
        if (!(name.equals(that.name) &&
                type.equals(that.type))) {
            return false;
        }
        if (!that.isMust && !(this.isMust.equals(that.getMust()))) {
            //属性相同以true的描述为准
            that.isMust = true;
            that.setDescribe(describe);
        }
        if (!that.isMust && (this.isMust.equals(that.getMust()))) {
            //相同
            if (StringUtils.isEmpty(that.getDescribe()) && !Objects.equals(that.getDescribe(), describe)) {
                that.setDescribe(describe);
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
