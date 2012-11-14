package se.kb.libris.utils.cqueue.test;

import java.util.logging.*;
import se.kb.libris.utils.cqueue.*;

public class TestQueue {
    public static void main(String args[]) throws Exception {
        new CQ(
            new Producer() {
                int n = 0;
                
                @Override
                public synchronized Work getWork() {
                    if (n++ >= 100) return null;
                    return new Work(String.valueOf(System.currentTimeMillis()));
                }
            }, new Consumer() {
                @Override
                public void consume(Work work) {
                    System.out.println(work.seqNo + " " + work.source + " -> " + work.result);
                }
            }, new WorkerFactory() {
                @Override
                public Worker createWorker() {
                    return new Worker() {
                        @Override
                        public Work doWork(Work work) {
                            try {
                                work.result = "#" + work.source + "#";
                                Thread.sleep((int)(100*Math.random()));
                                return work;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(TestQueue.class.getName()).log(Level.SEVERE, null, ex);
                                throw new RuntimeException(ex);
                            }
                        }
                    };
                }
        }).start().join();
    }
}
