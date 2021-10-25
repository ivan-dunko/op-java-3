package ru.nsu.opjava;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.*;
import javassist.bytecode.*;

public class TransactionProcessorTransformer implements ClassFileTransformer {

    private final String targetClassName;


    public TransactionProcessorTransformer(String name) {
        this.targetClassName = name;
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer){

        className = className.replaceAll("/", ".");

        if (!className.equals(targetClassName)) {
            return classfileBuffer;
        }

        byte[] byteCode = classfileBuffer;

        try {
            ClassPool cp = ClassPool.getDefault();
            cp.appendClassPath(new LoaderClassPath(loader));
            System.out.println();
            CtClass cc = cp.get(this.targetClassName);

            CtMethod m = cc.getDeclaredMethod("processTransaction");
            m.insertBefore("{txNum += 99;ru.nsu.opjava.Aggregator.startLog();}");
            m.insertAfter("ru.nsu.opjava.Aggregator.endLog();");

            m = cc.getDeclaredMethod("main");
            m.insertAfter("ru.nsu.opjava.Aggregator.printStats();");

            byteCode = cc.toBytecode();
            cc.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteCode;
    }
}
