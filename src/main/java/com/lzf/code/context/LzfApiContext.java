package com.lzf.code.context;

import com.lzf.code.common.LzfConstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import sun.tools.jar.resources.jar;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        } catch (ClassNotFoundException ignored) {
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
        URL url = LzfApiContext.class.getClassLoader().getResource(s);
        // 获取包的名字 并进行替换
        String packageDirName = scanner.replace('.', '/');
        assert url != null;
        // 得到协议的名称
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
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
        } else if ("jar".equals(protocol)) {
            // 如果是jar包文件
            try {
                // 获取jar
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                // 从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                // 同样的进行循环迭代
                while (entries.hasMoreElements()) {
                    // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    // 如果是以/开头的
                    if (name.charAt(0) == '/') {
                        // 获取后面的字符串
                        name = name.substring(1);
                    }
                    // 如果前半部分和定义的包名相同
                    if (name.startsWith(packageDirName)) {
                        // 如果是一个.class文件 而且不是目录
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            Class<?> clazz = getClazz(name.replace("/", ".").replace("\\", ".").replace(LzfConstance.CLASS, ""));
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
            } catch (Exception ignored) {
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
