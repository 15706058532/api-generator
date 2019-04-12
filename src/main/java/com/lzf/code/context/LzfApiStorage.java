package com.lzf.code.context;

import com.lzf.code.definition.LzfApi;
import com.lzf.code.definition.LzfApiParam;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 18:06
 *
 * @author Li Zhenfeng
 */
public class LzfApiStorage {
    /**
     * 存放所有的api接口
     */
    private List<LzfApi> lzfApiList = new ArrayList<>();
    /**
     * 存放uri对应方法的定义 key: 请求方式+uri,value:对应方法的定义
     */
    private Map<String, Method> uriMapping = new HashMap<String, Method>(256);

    /**
     * 存放接口下的入参
     */
    private Map<String, LzfApiParam> reqLzfApiParamMap = new HashMap<>(256);
    /**
     * 存放接口下的出参
     */
    private Map<String, LzfApiParam> respLzfApiParamMap = new HashMap<>(256);


    public List<LzfApi> getLzfApiList() {
        return lzfApiList;
    }

    public void setLzfApiList(List<LzfApi> lzfApiList) {
        this.lzfApiList = lzfApiList;
    }

    public Map<String, Method> getUriMapping() {
        return uriMapping;
    }

    /**
     * 根据uri取出对应方法的定义
     *
     * @param uri
     * @return
     */
    public Method getMethodByUri(String uri) {
        if (uriMapping.containsKey(uri)) {
            return uriMapping.get(uri);
        }
        return null;
    }

    public void setUriMapping(Map<String, Method> uriMapping) {
        this.uriMapping = uriMapping;
    }

    public void addUriMapping(String uri, Method method) {
        this.uriMapping.put(uri, method);
    }

    public Map<String, LzfApiParam> getReqLzfApiParamMap() {
        return reqLzfApiParamMap;
    }

    public void setReqLzfApiParamMap(Map<String, LzfApiParam> reqLzfApiParamMap) {
        this.reqLzfApiParamMap = reqLzfApiParamMap;
    }

    public void addReqLzfApiParamMap(Map<String, LzfApiParam> reqLzfApiParamMap) {
        this.reqLzfApiParamMap.putAll(reqLzfApiParamMap);
    }

    public Map<String, LzfApiParam> getRespLzfApiParamMap() {
        return respLzfApiParamMap;
    }

    public void setRespLzfApiParamMap(Map<String, LzfApiParam> respLzfApiParamMap) {
        this.respLzfApiParamMap = respLzfApiParamMap;
    }
}
