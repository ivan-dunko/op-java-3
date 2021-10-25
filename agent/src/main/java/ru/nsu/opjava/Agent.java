package ru.nsu.opjava;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import ru.nsu.opjava.TransactionProcessorTransformer;

public class Agent
{
    public static void premain(String agentArgs, Instrumentation inst) {
        String classname = "ru.nsu.opjava.TransactionProcessor";
        System.out.println("Agent is alive");
        //transformClass(classname, inst);

        inst.addTransformer(new TransactionProcessorTransformer(classname));
    }
/*
    private static void transformClass(
            String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        } catch (Exception ex) {
            //LOGGER.error("Class [{}] not found with Class.forName");
            ex.printStackTrace();
        }
        // otherwise iterate all loaded classes and find what we want
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException(
                "Failed to find class [" + className + "]");
    }
    */

//    private static void transform(
//            Class<?> clazz,
//            ClassLoader classLoader,
//            Instrumentation instrumentation) {
//        TransactionProcessorTransformer transformer = new TransactionProcessorTransformer(clazz.getName(), classLoader);
//        instrumentation.addTransformer(transformer, true);
//        try {
//            instrumentation.retransformClasses(clazz);
//        } catch (Exception ex) {
//            throw new RuntimeException(
//                    "Transform failed for: [" + clazz.getName() + "]", ex);
//        }
//    }
}
