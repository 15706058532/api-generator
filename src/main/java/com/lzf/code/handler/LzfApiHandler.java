package com.lzf.code.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lzf.code.annatation.*;
import com.lzf.code.common.LzfConstance;
import com.lzf.code.common.LzfSymbolConstant;
import com.lzf.code.context.LzfApiContext;
import com.lzf.code.context.LzfApiStorage;
import com.lzf.code.context.LzfParameterNameDiscoverer;
import com.lzf.code.definition.LzfApi;
import com.lzf.code.definition.LzfApiMethod;
import com.lzf.code.definition.LzfApiParam;
import com.lzf.code.definition.LzfApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.DateFormat;
import java.util.*;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 18:03
 *
 * @author Li Zhenfeng
 */
public class LzfApiHandler {
    @Autowired
    private LzfApiContext lzfApiContext;
    @Autowired
    private LzfApiStorage lzfApiStorage;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * 解析类
     */
    @PostConstruct
    public void doAnalysisClazz() {
        List<Class<?>> restControllerClasses = lzfApiContext.getRestControllerClasses();
        List<LzfApi> lzfApiList = new ArrayList<>();
        for (Class<?> restControllerClass : restControllerClasses) {
            Annotation[] annotations = restControllerClass.getAnnotations();
            String[] valueArr = new String[0];
            String description = "";
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestMapping) {
                    RequestMapping requestMapping = (RequestMapping) annotation;
                    valueArr = requestMapping.value();
                } else if (annotation instanceof LzfApiDescribe) {
                    LzfApiDescribe lzfapidescribe = (LzfApiDescribe) annotation;
                    description = lzfapidescribe.value();
                }
            }
            Method[] methods = restControllerClass.getMethods();
            List<LzfApiMethod> lzfApiMethods = new ArrayList<>();
            for (Method method : methods) {
                lzfApiMethods.addAll(doRestAnalysisMethod(method, valueArr));
            }
            if (StringUtils.isEmpty(description.trim()) && lzfApiMethods.isEmpty()) {
                continue;
            }
            String clazzName = restControllerClass.getSimpleName();
            if (StringUtils.isEmpty(description.trim())) {
            }
            lzfApiList.add(new LzfApi(clazzName, description, lzfApiMethods));
        }
        lzfApiStorage.setLzfApiList(lzfApiList);
    }

    /**
     * 解析方法
     */
    private List<LzfApiMethod> doRestAnalysisMethod(Method method, String[] valueArr) {
        Annotation[] annotations = method.getAnnotations();
        String[] methodValueAddr = new String[0];
        String methodDescribe = "";
        List<String> requestTypes = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GetMapping) {
                requestTypes.add(LzfConstance.GET);
                GetMapping getMapping = (GetMapping) annotation;
                methodValueAddr = getMapping.value();
            } else if (annotation instanceof PostMapping) {
                requestTypes.add(LzfConstance.POST);
                PostMapping postMapping = (PostMapping) annotation;
                methodValueAddr = postMapping.value();
            } else if (annotation instanceof PutMapping) {
                requestTypes.add(LzfConstance.PUT);
                PutMapping putMapping = (PutMapping) annotation;
                methodValueAddr = putMapping.value();
            } else if (annotation instanceof DeleteMapping) {
                requestTypes.add(LzfConstance.DELETE);
                DeleteMapping deleteMapping = (DeleteMapping) annotation;
                methodValueAddr = deleteMapping.value();
            } else if (annotation instanceof RequestMapping) {
                requestTypes.add(LzfConstance.GET);
                requestTypes.add(LzfConstance.POST);
                requestTypes.add(LzfConstance.PUT);
                requestTypes.add(LzfConstance.DELETE);
                RequestMapping requestMapping = (RequestMapping) annotation;
                methodValueAddr = requestMapping.value();
            } else if (annotation instanceof LzfApiDescribe) {
                LzfApiDescribe lzfApiMethodDescribe = (LzfApiDescribe) annotation;
                methodDescribe = lzfApiMethodDescribe.value();
            }
        }
        List<LzfApiMethod> lzfApiMethods = new ArrayList<>();
        for (String value : valueArr) {
            for (String methodValue : methodValueAddr) {
                for (String requestType : requestTypes) {
                    //把uri和方法对应关系保存起来
                    lzfApiStorage.addUriMapping(requestType + contextPath + value + methodValue, method);
                    LzfApiMethod lzfApiMethod = new LzfApiMethod(requestType, contextPath + value + methodValue, methodDescribe);
                    lzfApiMethods.add(lzfApiMethod);
                }
            }
        }
        return lzfApiMethods;
    }

    /**
     * 解析请求参数
     *
     * @param uri
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<LzfApiParam> doAnalysisMethodParam(String uri) throws IOException, ClassNotFoundException {
        Method method = lzfApiStorage.getMethodByUri(uri);
        Annotation[] annotations = method.getAnnotations();
        //必须的类
        String[] musts = new String[0];
        //非必须的类
        String[] noMusts = new String[0];
        for (Annotation annotation : annotations) {
            if (annotation instanceof LzfApiRequest) {
                //@LzfApiRequest配置的优先级要高于@LzfApiDescribe
                LzfApiRequest lzfApiRequest = (LzfApiRequest) annotation;
                musts = lzfApiRequest.musts();
                noMusts = lzfApiRequest.noMusts();
            }
        }
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        LzfParameterNameDiscoverer discoverer = new LzfParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        List<LzfApiParam> lzfApiParams = new ArrayList<>();
        //url类型的参数也可以是多个  多个的情况下需进行合并
        //form-data类型的参数可以是多个 多个的情况下需进行合并
        //json类型的参数只能有一个
        //对多个form-data的参数进行合并去重 有一个为必须则为必须
        Set<LzfApiProperty> formDataLzfApiProperties = new HashSet<>();
        Set<LzfApiProperty> urlLzfApiProperties = new HashSet<>();
        Set<LzfApiProperty> jsonLzfApiProperties = new HashSet<>();
        String formDataFormat = null;
        String urlFormat = null;
        String jsonFormat = null;
        for (int i = 0; i < parameterNames.length; i++) {
            LzfApiParam lzfApiParam = doAnalysisProperty(parameterAnnotations[i], genericParameterTypes[i], parameterNames[i]);
            List<String> mustList = Arrays.asList(musts);
            List<String> noMustList = Arrays.asList(noMusts);
            List<LzfApiProperty> removeList = new ArrayList<>();
            if (musts.length > 0 && noMusts.length > 0) {
                //将配置了必填属性和非必填属性的保留其他的移除   *表示全部保留
                //必填的must设置为true   非必填的must设置为false
                for (LzfApiProperty lzfApiProperty : lzfApiParam.getLzfApiProperties()) {
                    //必填字段由于非必填字段
                    if (mustList.contains(LzfSymbolConstant.ASTERISK) || mustList.contains(lzfApiProperty.getName())) {
                        lzfApiProperty.setMust(true);
                    } else if (noMustList.contains(LzfSymbolConstant.ASTERISK) || noMustList.contains(lzfApiProperty.getName())) {
                        lzfApiProperty.setMust(false);
                    } else {
                        removeList.add(lzfApiProperty);
                    }
                }
            } else if (musts.length > 0) {
                //必填的must设置为true  *表示全部保留 未配置的移除
                for (LzfApiProperty lzfApiProperty : lzfApiParam.getLzfApiProperties()) {
                    if (mustList.contains(LzfSymbolConstant.ASTERISK) || mustList.contains(lzfApiProperty.getName())) {
                        lzfApiProperty.setMust(true);
                    } else {
                        removeList.add(lzfApiProperty);
                    }
                }
            } else if (noMusts.length > 0) {
                //非必填的must设置为false, *表示全部保留 未配置的移除
                for (LzfApiProperty lzfApiProperty : lzfApiParam.getLzfApiProperties()) {
                    if (noMustList.contains(LzfSymbolConstant.ASTERISK) || noMustList.contains(lzfApiProperty.getName())) {
                        lzfApiProperty.setMust(false);
                    } else {
                        removeList.add(lzfApiProperty);
                    }
                }
            }
            lzfApiParam.getLzfApiProperties().removeAll(removeList);

            if (Objects.equals(lzfApiParam.getFormat(), LzfConstance.FORM_DATA)) {
                formDataLzfApiProperties.addAll(lzfApiParam.getLzfApiProperties());
                formDataFormat = lzfApiParam.getFormat();
            } else if (Objects.equals(lzfApiParam.getFormat(), LzfConstance.FORM_URL)) {
                urlLzfApiProperties.addAll(lzfApiParam.getLzfApiProperties());
                urlFormat = lzfApiParam.getFormat();
            } else {
                jsonFormat = lzfApiParam.getFormat();
                jsonLzfApiProperties.addAll(lzfApiParam.getLzfApiProperties());
            }
        }
        if (!formDataLzfApiProperties.isEmpty()) {
            LzfApiParam lzfApiParam = new LzfApiParam();
            lzfApiParam.setFormat(formDataFormat);
            lzfApiParam.addLzfApiPropertiesAll(formDataLzfApiProperties);
            lzfApiParams.add(lzfApiParam);

        }
        if (!urlLzfApiProperties.isEmpty()) {
            LzfApiParam lzfApiParam = new LzfApiParam();
            lzfApiParam.setFormat(urlFormat);
            lzfApiParam.addLzfApiPropertiesAll(urlLzfApiProperties);
            lzfApiParams.add(lzfApiParam);

        }
        if (!jsonLzfApiProperties.isEmpty()) {
            LzfApiParam lzfApiParam = new LzfApiParam();
            lzfApiParam.setFormat(jsonFormat);
            lzfApiParam.addLzfApiPropertiesAll(jsonLzfApiProperties);
            lzfApiParams.add(lzfApiParam);

        }
        return lzfApiParams;
    }

    /**
     * 返回值解析
     */
    public LzfApiParam doAnalysisMethodReturn(String uri) throws ClassNotFoundException {
        Method method = lzfApiStorage.getMethodByUri(uri);
        //先判断是否有@LzfAPiResponse，有就不去判断返回值类型了
        Annotation[] annotations = method.getAnnotations();
        String dataName = null;
        String pageName = null;
        Class<?> dataClazz = null;
        ClassType[] dataClassTypes = new ClassType[0];
        Class<?> pageClazz = null;
        ClassType[] pageClazzTypes = new ClassType[0];
        for (Annotation annotation : annotations) {
            if (annotation instanceof LzfApiResponse) {
                LzfApiResponse lzfApiResponse = (LzfApiResponse) annotation;
                dataName = lzfApiResponse.name();
                dataClazz = lzfApiResponse.clazz();
                dataClassTypes = lzfApiResponse.clazzType();
                pageName = lzfApiResponse.pageName();
                pageClazz = lzfApiResponse.pageClazz();
                pageClazzTypes = lzfApiResponse.pageClazzType();

            }
        }


        Type genericReturnType = method.getGenericReturnType();
        LzfApiParam lzfApiParam = new LzfApiParam();
        lzfApiParam.setFormat(LzfConstance.JSON);
        List<String> arrays = new ArrayList<>();
        Type baseType = getBaseType(genericReturnType, arrays);
        String typeValue = sortBaseType(baseType);
        if (Objects.equals(typeValue, LzfConstance.UNDEFINED)) {
            typeValue = LzfConstance.OBJECT;
        } else if (Objects.equals(typeValue, LzfConstance.OBJECT)) {
            lzfApiParam.setType(LzfConstance.JSON + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
            List<LzfApiProperty> lzfApiProperties = analysisProperty(Class.forName(baseType.getTypeName()));
            if (!StringUtils.isEmpty(dataName)) {
                for (LzfApiProperty lzfApiProperty : lzfApiProperties) {
                    if (Objects.equals(lzfApiProperty.getName(), dataName)) {
                        lzfApiProperty.setType(LzfConstance.OBJECT + (dataClassTypes.length > 0 ? "数组" + (dataClassTypes.length > 1 ? "X" + dataClassTypes.length : "") : ""));
                        lzfApiProperty.setClassName(dataClazz.getName());
                    }
                    if (Objects.equals(lzfApiProperty.getName(), pageName)) {
                        if (!StringUtils.isEmpty(lzfApiProperty.getType())) {
                            lzfApiProperty.setType(LzfConstance.OBJECT + (pageClazzTypes.length > 0 ? "数组" + (pageClazzTypes.length > 1 ? "X" + pageClazzTypes.length : "") : "") + "|" + pageName + ":" + pageClazz.getName());
                            lzfApiProperty.setClassName(dataClazz.getName() + "|" + pageName + ":" + pageClazz.getName());
                        } else {
                            lzfApiProperty.setClassName(pageClazz.getName());
                        }
                    }
                }
            }
            lzfApiParam.setLzfApiProperties(lzfApiProperties);
            return lzfApiParam;
        } else {
            lzfApiParam.setType(LzfConstance.JSON + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
        }
        return lzfApiParam;
    }

    /**
     * 请求参数解析
     *
     * @param annotations
     * @param genericParameterType
     * @param properTyName
     * @return
     * @throws ClassNotFoundException
     */
    private LzfApiParam doAnalysisProperty(Annotation[] annotations, Type genericParameterType, String properTyName) throws ClassNotFoundException {
        LzfApiParam lzfApiParam = new LzfApiParam();
        String format = LzfConstance.FORM_DATA;
        String describe = "";
        String pattern = null;
        boolean must = false;

        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestBody) {
                format = LzfConstance.JSON;
            } else if (annotation instanceof PathVariable) {
                format = LzfConstance.FORM_URL;
                PathVariable pathVariable = (PathVariable) annotation;
                if (!StringUtils.isEmpty(pathVariable.value())) {
                    properTyName = pathVariable.value();
                }
                must = pathVariable.required();
            } else if (annotation instanceof LzfApiDescribe) {
                LzfApiDescribe lzfApiDescribe = (LzfApiDescribe) annotation;
                if (!StringUtils.isEmpty(lzfApiDescribe.value())) {
                    describe = lzfApiDescribe.value();
                }
                must = lzfApiDescribe.must();
            } else if (annotation instanceof RequestParam) {
                RequestParam requestParam = (RequestParam) annotation;
                String name = requestParam.name();
                must = requestParam.required();
                if (!StringUtils.isEmpty(name)) {
                    properTyName = name;
                }
            } else if (annotation instanceof JsonFormat) {
                JsonFormat jsonFormat = (JsonFormat) annotation;
                if (!StringUtils.isEmpty(jsonFormat.pattern())) {
                    pattern = jsonFormat.pattern();
                }
            } else if (annotation instanceof DateTimeFormat) {
                DateTimeFormat dateTimeFormat = (DateTimeFormat) annotation;
                //jsonFormat优先
                if (!StringUtils.isEmpty(dateTimeFormat.pattern()) && StringUtils.isEmpty(pattern)) {
                    pattern = dateTimeFormat.pattern();
                }
            }
        }
        List<String> arrays = new ArrayList<>();
        Type baseType = getBaseType(genericParameterType, arrays);
        String typeValue = sortBaseType(baseType);
        if (Objects.equals(typeValue, LzfConstance.UNDEFINED)) {
            typeValue = LzfConstance.OBJECT;
        } else if (Objects.equals(typeValue, LzfConstance.OBJECT)) {
            if (Objects.equals(format, LzfConstance.FORM_URL)) {
                lzfApiParam.setFormat(format);
            }
            lzfApiParam.setFormat(typeValue + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
            //多层数组也只需要最终的那个对象全名
            LzfApiProperty lzfApiProperty = new LzfApiProperty();
            lzfApiProperty.setClassName(baseType.getTypeName());
            if (Objects.equals(format, LzfConstance.FORM_DATA) && arrays.size() > 0) {
                //from-data不能是数组
                lzfApiProperty.setError(true);
                lzfApiProperty.setErrorMsg("from-data类型的参数不能是对象数组");
                lzfApiParam.addLzfApiPropertie(lzfApiProperty);
                return lzfApiParam;
            } else {
                List<LzfApiProperty> lzfApiProperties = analysisProperty(Class.forName(baseType.getTypeName()));
                lzfApiParam.setFormat(format + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
                lzfApiParam.setLzfApiProperties(lzfApiProperties);
                return lzfApiParam;
            }
        } else {
            if (Objects.equals(typeValue, LzfConstance.DATE) && Objects.nonNull(pattern)) {
                typeValue += "(" + pattern + ")";
            }
            lzfApiParam.setFormat(format + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
            if (Objects.equals(format, LzfConstance.FORM_DATA) && arrays.size() > 1) {
                //from-data不能是数组类型的数组
                LzfApiProperty lzfApiProperty = new LzfApiProperty();
                lzfApiProperty.setError(true);
                lzfApiProperty.setErrorMsg("from-data不能是数组类型的数组");
                lzfApiParam.addLzfApiPropertie(lzfApiProperty);
            } else if (Objects.equals(format, LzfConstance.FORM_DATA) && arrays.size() == 1) {
                if (Objects.equals(arrays.get(0), LzfConstance.TYPE_COLLECTION)) {
                    LzfApiProperty lzfApiProperty = new LzfApiProperty();
                    lzfApiProperty.setError(true);
                    lzfApiProperty.setErrorMsg("from-data不能为集合类型");
                    lzfApiParam.addLzfApiPropertie(lzfApiProperty);
                    return lzfApiParam;
                }
            }
            LzfApiProperty lzfApiProperty = new LzfApiProperty();
            if (Objects.equals(format, LzfConstance.FORM_URL)) {
                lzfApiParam.setFormat(format);
            }
            lzfApiProperty.setType(typeValue + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
            lzfApiProperty.setName(properTyName);
            lzfApiProperty.setMust(must);
            lzfApiProperty.setDescribe(describe);
            lzfApiParam.addLzfApiPropertie(lzfApiProperty);
        }
        return lzfApiParam;
    }

    public List<LzfApiProperty> analysisProperty(Class<?> aClass) {
        List<LzfApiProperty> lzfApiProperties = new ArrayList<>();
        Method[] methods = aClass.getMethods();
        List<String> fieldNames = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set")) {
                fieldNames.add(name.replaceFirst("set", "").toLowerCase());
            } else if (name.startsWith("is")) {
                fieldNames.add(name.replaceFirst("is", "").toLowerCase());
            }
        }
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!fieldNames.contains(field.getName().toLowerCase())) {
                //没有set方法跳过
                continue;
            }
            LzfApiProperty lzfApiProperty = new LzfApiProperty();
            Annotation[] fieldAnnotations = field.getAnnotations();
            String pattern = null;
            for (Annotation fieldAnnotation : fieldAnnotations) {
                //解析属性注释
                if (fieldAnnotation instanceof LzfApiDescribe) {
                    LzfApiDescribe lzfApiDescribe = (LzfApiDescribe) fieldAnnotation;
                    lzfApiProperty.setDescribe(lzfApiDescribe.value());
                    lzfApiProperty.setMust(lzfApiDescribe.must());
                } else if (fieldAnnotation instanceof JsonFormat) {
                    JsonFormat jsonFormat = (JsonFormat) fieldAnnotation;
                    if (!StringUtils.isEmpty(jsonFormat.pattern())) {
                        pattern = jsonFormat.pattern();
                    }
                } else if (fieldAnnotation instanceof DateTimeFormat) {
                    DateTimeFormat dateTimeFormat = (DateTimeFormat) fieldAnnotation;
                    //jsonFormat优先
                    if (!StringUtils.isEmpty(dateTimeFormat.pattern()) && StringUtils.isEmpty(pattern)) {
                        pattern = dateTimeFormat.pattern();
                    }
                }
            }
            List<String> arrays = new ArrayList<>();
            lzfApiProperty.setName(field.getName());
            Type baseType = getBaseType(field.getGenericType(), arrays);
            String typeValue = sortBaseType(baseType);
            if (Objects.equals(typeValue, LzfConstance.UNDEFINED)) {
                typeValue = LzfConstance.OBJECT;
            } else if (Objects.equals(typeValue, LzfConstance.OBJECT)) {
                //多层数组也只需要最终的那个对象全名
                lzfApiProperty.setClassName(baseType.getTypeName());
            }
            if (Objects.equals(typeValue, LzfConstance.DATE) && Objects.nonNull(pattern)) {
                typeValue += "(" + pattern + ")";
            }
            lzfApiProperty.setType(typeValue + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
            lzfApiProperties.add(lzfApiProperty);
        }
        return lzfApiProperties;
    }

    /**
     * 对最终类型分类
     *
     * @param type
     * @return
     */
    public String sortBaseType(Type type) {
        if (type == int.class || type == Integer.class
                || type == long.class || type == Long.class
                || type == short.class || type == Short.class) {
            return LzfConstance.NUMBER;
        } else if (type == float.class || type == Float.class
                || type == double.class || type == Double.class) {
            return LzfConstance.DOUBLE;
        } else if (type == boolean.class || type == Boolean.class) {
            return LzfConstance.BOOLEAN;
        } else if (type == byte.class || type == Byte.class) {
            return LzfConstance.BYTE;
        } else if (type == String.class
                || type == char.class || type == Character.class) {
            return LzfConstance.STRING;
        } else if (type == Date.class) {
            return LzfConstance.DATE;
        } else if (type == MultipartFile.class) {
            return LzfConstance.FILE;
        } else if (type == File.class) {
            return LzfConstance.FILE;
        } else if (type == Map.class) {
            //无法正常解析属性
            return LzfConstance.UNDEFINED;
        } else if (type == Object.class) {
            //无法正常解析属性
            return LzfConstance.UNDEFINED;
        } else {
            if (type.getTypeName().contains(LzfSymbolConstant.RIGHT_ANGLE_BRACKET)
                    || type.getTypeName().contains(LzfSymbolConstant.RIGHT_SQUARE_BRACKET)) {
                //无法正常解析属性
                return LzfConstance.UNDEFINED;
            }
            return LzfConstance.OBJECT;
        }
    }

    /**
     * 获取最终类型
     *
     * @param genericReturnType
     * @param classList
     * @return
     */
    public static Type getBaseType(Type genericReturnType, List<String> classList) {
        Objects.requireNonNull(genericReturnType);
        if (genericReturnType instanceof ParameterizedType
                && Collection.class.isAssignableFrom((Class) (((ParameterizedType) genericReturnType).getRawType()))) {
            //Collection的子类也会进来
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            Type type = actualTypeArguments[0];
            classList.add("java.util.Collection");
            return getBaseType(type, classList);
        } else if (genericReturnType instanceof ParameterizedType
                && Map.class.isAssignableFrom((Class) (((ParameterizedType) genericReturnType).getRawType()))) {
            //Map的子类也会进来
            try {
                return Class.forName("java.util.Map");
            } catch (ClassNotFoundException ignored) {
            }
            return genericReturnType;
        } else {
            //一般来说，判断是否是某种类型是可以使用isAssignableFrom
            // 判断是否是数组类型比较特殊，要使用isArray()这个函数
            if ((genericReturnType instanceof Class) && ((Class) genericReturnType).isArray()) {
                //获得数组的类型，使用getComponentType()这个方法
                classList.add(((Class) genericReturnType).getComponentType().getTypeName() + "[]");
                return getBaseType(((Class) genericReturnType).getComponentType(), classList);
            }
            return genericReturnType;
        }
    }

    public List<LzfApiProperty> doAnalysisProperties(String className) throws ClassNotFoundException {
        return analysisProperty(Class.forName(className));
    }
}
