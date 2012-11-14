package se.kb.libris.utils.cqueue.test;

import java.util.logging.*;
import se.kb.libris.utils.cqueue.*;

public class TestQueue {
    public static void main(String args[]) throws Exception {
        final int MAX_ITERATIONS = 10000;
        
        for (int i=0;i<6;i++) {
            int maxThreads = (int)Math.pow(2, i);
            long t0 = System.currentTimeMillis();

            new CQ(
                new Producer() {
                    int n = 0;

                    @Override
                    public Work getWork() {
                        if (n++ >= MAX_ITERATIONS) return null;
                        return new Work(String.valueOf(System.currentTimeMillis()));
                    }
                }, new Consumer() {
                    @Override
                    public void consume(Work work) {
                        //System.out.println(work.seqNo + " " + work.source + " -> " + work.result);
                    }
                }, new WorkerFactory() {
                    @Override
                    public Worker createWorker() {
                        return new Worker() {
                            @Override
                            public Work doWork(Work work) {
                                for (int i=0;i<100;i++)
                                    work.result = "" + work.result + " " + work.source;
                                
                                return work;
                            }
                        };
                    }
            },
            maxThreads,
            maxThreads).start().join();
            
            long delta = System.currentTimeMillis() - t0;
            
            System.out.println("" + MAX_ITERATIONS + " iterations completed with " + maxThreads + " threads in " + delta + " msecs. @ " + (1000 * MAX_ITERATIONS) / delta + " recs/sec.");
        }
    }
}
