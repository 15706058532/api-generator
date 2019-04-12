package com.lzf.code.context;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;


/**
 * 参数名获取器
 * <br/>
 * Created in 2018-11-08 13:56
 *
 * @author Li Zhenfeng
 */
public class LzfParameterNameDiscoverer {
    private Method method;
    private String[] methodParametersNames;

    public String[] getParameterNames(Method method) throws IOException {
        this.method = method;
        methodParametersNames = new String[method.getParameterTypes().length];
        String className = method.getDeclaringClass().getName();
        //在tomcat服务器中由于类加载器不同所以用这种方法不行得用文件流读取才行
        ClassReader cr = new ClassReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(className.replace('.', '/') + ".class")));
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        DefaultClassAdapter defaultClassAdapter = new DefaultClassAdapter(cw);
        cr.accept(defaultClassAdapter, 0);
        return methodParametersNames;
    }

    public class DefaultClassAdapter extends ClassAdapter {

        DefaultClassAdapter(ClassVisitor classVisitor) {
            super(classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            final Type[] argTypes = Type.getArgumentTypes(desc);
            //参数类型不一致
            if (!method.getName().equals(name) || !matchTypes(argTypes, method.getParameterTypes())) {
                return mv;
            }
            return new DefaultMethodAdapter(mv);
        }

        /**
         * 比较参数是否一致
         */
        private boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
            if (types.length != parameterTypes.length) {
                return false;
            }
            for (int i = 0; i < types.length; i++) {
                if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                    return false;
                }
            }
            return true;
        }

        public class DefaultMethodAdapter extends MethodAdapter {
            DefaultMethodAdapter(MethodVisitor methodVisitor) {
                super(methodVisitor);
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                //如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
                int methodParameterIndex = Modifier.isStatic(method.getModifiers()) ? index : index - 1;
                if (0 <= methodParameterIndex && methodParameterIndex < method.getParameterTypes().length) {
                    methodParametersNames[methodParameterIndex] = name;
                }
                super.visitLocalVariable(name, desc, signature, start, end, index);
            }
        }
    }
}
