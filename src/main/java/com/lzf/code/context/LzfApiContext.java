package com.lzf.code.context;

import com.lzf.code.common.LzfConstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 16:13
 *
 * @author Li Zhenfeng
 */
public class LzfApiContext {
    /**
     * 存储扫描RestController
     */
    private List<Class<?>> restControllerClasses = new ArrayList<>();
    /**
     * 存储扫描Controller
     */
    private List<Class<?>> controllerClasses = new ArrayList<>();
    /**
     * 启动类
     */
    private Class<?> mainApplicationClass;

    public LzfApiContext() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    mainApplicationClass = Class.forName(stackTraceElement.getClassName());
                    doScanner();
                }
            }
        } catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
    }

    /**
     * 扫描目录下包括子目录下所有的class
     */
    private void doScanner() {
        if (Objects.nonNull(mainApplicationClass)) {
            doClassFile(mainApplicationClass.getPackage().getName());
        }
    }

    /**
     * 递归扫描
     *
     * @param scanner 目录名
     */
    private void doClassFile(String scanner) {
        String s = scanner.replace(".", "/");
        URL url = LzfApiContext.class.getClassLoader().getResource( s);
        assert url != null;
        File[] files = new File(url.getFile()).listFiles();
        assert files != null;
        for (File classFile : files) {
            if (classFile.isDirectory()) {
                doClassFile(scanner + "." + classFile.getName());
                continue;
            }
            String path = classFile.getPath();
            if (path.endsWith(LzfConstance.CLASS)) {
                Class<?> clazz = getClazz(scanner + "." + classFile.getName().replace(LzfConstance.CLASS, ""));
                if (Objects.nonNull(clazz)) {
                    if (clazz.isAnnotationPresent(RestController.class)) {
                        restControllerClasses.add(clazz);
                    } else if (clazz.isAnnotationPresent(Controller.class)) {
                        controllerClasses.add(clazz);
                    }
                }
            }
        }
    }

    /**
     * 创建类的定义
     *
     * @param beanClassName 类全名
     * @return 返回类的定义
     */
    private Class<?> getClazz(String beanClassName) {
        try {
            return Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Class<?>> getRestControllerClasses() {
        return restControllerClasses;
    }

    public List<Class<?>> getControllerClasses() {
        return controllerClasses;
    }
}
