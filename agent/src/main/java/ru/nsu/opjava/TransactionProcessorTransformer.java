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
    private final ClassLoader targetClassLoader;

    public TransactionProcessorTransformer(String name, ClassLoader classLoader) {
        this.targetClassName = name;
        this.targetClassLoader = classLoader;
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer){
        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName
                .replaceAll("\\.", "/");
        /*
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }
        */

        if (loader.equals(targetClassLoader)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));
                System.out.println();
                CtClass cc = cp.get(this.targetClassName);

                CtMethod m = cc.getDeclaredMethod("processTransaction");
                m.insertBefore("txNum += 99;");

                ClassFile cf = cc.getClassFile();
                MethodInfo minfo = cf.getMethod("main");
                CodeAttribute ca = minfo.getCodeAttribute();
                CodeIterator ci = ca.iterator();

                int sind = cf.getConstPool().addStringInfo("Fancy string");
                String info = cf.getConstPool().getStringInfo(sind);
                System.out.println(info);
                int ivByte = 0;
                while (ci.hasNext()){
                    int index = ci.next();
                    int op = ci.byteAt(index);

                    // invoke virtual
                    if (op == 0xb6){
                        ivByte = index;
                        break;
                    }
                }

                //cf.getConstPool().print();

                byte printBytes[] = {
                        (byte) 0xb2, // getstatic
                        0x0,
                        0x7, // err
                        0x12, // ldc
                        (byte)(sind), // Fancy string
                        (byte) 0xb6, // invoke virtual
                        0x0,
                        29 // println
                };
                byte[] bb = {3};
                ci.insert(ivByte, bb);
                //int s = ca.computeMaxStack();
                ca.setMaxStack(100);
                minfo.setCodeAttribute(ca);
                //minfo.rebuildStackMapIf6(cp, cf);
                //minfo.rebuildStackMap(cp);
                byteCode = cc.toBytecode();
                cc.detach();

                File classFile = new File("Class.class");
                BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(classFile));
                byte[] data = byteCode;
                w.write(data, 0, data.length);
                w.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return byteCode;
    }
}
