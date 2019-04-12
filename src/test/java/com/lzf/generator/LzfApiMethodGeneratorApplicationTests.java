//package com.lzf.generator;
//
//import com.lzf.code.common.LzfConstance;
//import com.lzf.code.common.LzfSymbolConstant;
//import com.lzf.code.definition.LzfApiProperty;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringRunner.class)
//public class LzfApiMethodGeneratorApplicationTests {
//
//    @Test
//    public void contextLoads() {
//        List<LzfApiProperty> lzfApiProperties = analysisProperty(User.class);
//        for (LzfApiProperty lzfApiProperty : lzfApiProperties) {
//            System.out.println(lzfApiProperty);
//        }
//    }
//
//    public List<LzfApiProperty> analysisProperty(Class<?> aClass) {
//        List<LzfApiProperty> lzfApiProperties = new ArrayList<>();
//        Field[] declaredFields = aClass.getDeclaredFields();
//        for (Field field : declaredFields) {
//            LzfApiProperty lzfApiProperty = new LzfApiProperty();
//            List<String> arrays = new ArrayList<>();
//            //System.out.println("属性名称:" + field.getName());
//            lzfApiProperty.setName(field.getName());
//            //根据arrays.size()判断数组的层数
//            Type baseType = getBaseType(field.getGenericType(), arrays);
//            //System.out.println("中间类型:" + arrays);
//            String typeValue = sortType(baseType);
//            if (Objects.equals(typeValue, LzfConstance.UNDEFINED)) {
//                typeValue = LzfConstance.OBJECT;
//            } else if (Objects.equals(typeValue, LzfConstance.OBJECT)) {
//                //多层数组也只需要最终的那个对象全名
//                lzfApiProperty.setClassName(baseType.getTypeName());
//            }
//            lzfApiProperty.setType(typeValue + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
//            //System.out.println("最终类型:" + sortBaseType(baseType) + (arrays.size() > 0 ? "数组" + (arrays.size() > 1 ? "X" + arrays.size() : "") : ""));
//            //System.out.println("------------------------------------------");
//            lzfApiProperties.add(lzfApiProperty);
//        }
//        return lzfApiProperties;
//    }
//
//    /**
//     * 分类
//     *
//     * @param type
//     * @return
//     */
//    public String sortType(Type type) {
//        if (type == int.class || type == Integer.class
//                || type == long.class || type == Long.class
//                || type == short.class || type == Short.class) {
//            return LzfConstance.NUMBER;
//        } else if (type == float.class || type == Float.class
//                || type == double.class || type == Double.class) {
//            return LzfConstance.DOUBLE;
//        } else if (type == boolean.class || type == Boolean.class) {
//            return LzfConstance.BOOLEAN;
//        } else if (type == byte.class || type == Byte.class) {
//            return LzfConstance.BYTE;
//        } else if (type == String.class
//                || type == char.class || type == Character.class) {
//            return LzfConstance.STRING;
//        } else if (type == Map.class) {
//            //无法正常解析属性
//            return LzfConstance.UNDEFINED;
//        } else if (type == Object.class) {
//            //无法正常解析属性
//            return LzfConstance.UNDEFINED;
//        } else {
//            if (type.getTypeName().contains(LzfSymbolConstant.RIGHT_ANGLE_BRACKET)
//                    || type.getTypeName().contains(LzfSymbolConstant.RIGHT_SQUARE_BRACKET)) {
//                //无法正常解析属性
//                return LzfConstance.UNDEFINED;
//            }
//            return LzfConstance.OBJECT;
//        }
//    }
//
//    /**
//     * 获取最终类型
//     *
//     * @param genericReturnType
//     * @param classList
//     * @return
//     */
//    public static Type getBaseType(Type genericReturnType, List<String> classList) {
//        Objects.requireNonNull(genericReturnType);
//        /*if (genericReturnType instanceof ParameterizedType &&
//                List.class.isAssignableFrom((Class) (((ParameterizedType) genericReturnType).getRawType()))) {
//            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
//            Type type = actualTypeArguments[0];
//            classList.add("java.util.List");
//            return getBaseType(type, classList);
//        } else if (genericReturnType instanceof ParameterizedType &&
//                Set.class.isAssignableFrom((Class) (((ParameterizedType) genericReturnType).getRawType()))) {
//            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
//            Type type = actualTypeArguments[0];
//            classList.add("java.util.Set");
//            return getBaseType(type, classList);
//        } else */
//        if (genericReturnType instanceof ParameterizedType
//                && Collection.class.isAssignableFrom((Class) (((ParameterizedType) genericReturnType).getRawType()))) {
//            //Collection的子类也会进来
//            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
//            Type type = actualTypeArguments[0];
//            classList.add("java.util.Collection");
//            return getBaseType(type, classList);
//        } else if (genericReturnType instanceof ParameterizedType
//                && Map.class.isAssignableFrom((Class) (((ParameterizedType) genericReturnType).getRawType()))) {
//            //Map的子类也会进来
//            try {
//                return Class.forName("java.util.Map");
//            } catch (ClassNotFoundException ignored) {
//            }
//            return genericReturnType;
//        } else {
//            //一般来说，判断是否是某种类型是可以使用isAssignableFrom
//            // 判断是否是数组类型比较特殊，要使用isArray()这个函数
//            if ((genericReturnType instanceof Class) && ((Class) genericReturnType).isArray()) {
//                //获得数组的类型，使用getComponentType()这个方法
//                classList.add(((Class) genericReturnType).getComponentType().getTypeName() + "[]");
//                return getBaseType(((Class) genericReturnType).getComponentType(), classList);
//            }
//            return genericReturnType;
//        }
//    }
//
//    /**
//     * 以获得List<T>的类型为例子
//     */
//    public void getListType() throws NoSuchFieldException {
//        Field field = User.class.getDeclaredField("list");
//        //如果类似于List<String>这样的类型就是一种GenericType
//        //注意这是一种Type类型
//        Type type = field.getGenericType();
//        if (type instanceof ParameterizedType) {
//            //泛型参数类型
//            ParameterizedType parameterizedType = (ParameterizedType) type;
//            Type[] actualTypes = parameterizedType.getActualTypeArguments();
//            //因为List<String>获得第一个泛型参数,因为只有一个，我们取第一个
//            //如果我们有多个泛型参数，我们可以根据顺序取不同的泛型参数
//            assertEquals(actualTypes[0], String.class);
//            //如果获得List这个原始类型呢？
//            assertEquals(parameterizedType.getRawType(), List.class);
//        } else {
//            throw new IllegalStateException();
//        }
//    }
//
//    /**
//     * 数组类型
//     *
//     * @throws NoSuchFieldException
//     */
//    @Test
//    public void queryArrayType() throws NoSuchFieldException {
//        Field field = User.class.getDeclaredField("arrayCollectionInteger");
//        Class<?> type = field.getType();
//        //一般来说，判断是否是某种类型是可以使用isAssignableFrom
//        // 判断是否是数组类型比较特殊，要使用isArray()这个函数
//        if (type.isArray()) {
//            //获得数组的类型，使用getComponentType()这个方法
//            Class<?> componentType = type.getComponentType();
//            List<String> classList = new ArrayList<>();
//            System.out.println(getBaseType(type, classList));
//            System.out.println("****" + componentType.getTypeName());
//        } else {
//            throw new IllegalStateException();
//        }
//    }
//
//    /**
//     * 普通类型的变量直接field.getType()即可以获取到他们的类型
//     */
//    public void queryNameType() throws NoSuchFieldException {
//        Field field = User.class.getDeclaredField("name");
//        Class<?> type = field.getType();
//        assertEquals(type, String.class);
//    }
//}
