package ru.nsu.opjava;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Aggregator {
    private static long t = 0;
    private static long mint = Long.MAX_VALUE;
    private static long maxt = Long.MIN_VALUE;
    private static long sumt = 0;
    private static int cnt = 0;

    public static void startLog(){
        t = System.currentTimeMillis();
    }

    public static void endLog(){
        t = System.currentTimeMillis() - t;
        ++cnt;
        sumt += t;
        mint = min(mint, t);
        maxt = max(maxt, t);
    }

    public static void printStats(){
        sumt /= cnt;

        System.out.println("Average time: " + sumt);
        System.out.println("Minimal time: " + mint);
        System.out.println("Maximal time: " + maxt);
    }
}
