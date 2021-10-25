package ru.nsu.opjava;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import ru.nsu.opjava.TransactionProcessorTransformer;

public class Agent
{
    public static void premain(String agentArgs, Instrumentation inst) {
        String classname = "ru.nsu.opjava.TransactionProcessor";
        inst.addTransformer(new TransactionProcessorTransformer(classname));
    }

}
