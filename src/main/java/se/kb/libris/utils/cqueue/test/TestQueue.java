package se.kb.libris.utils.cqueue.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import se.kb.libris.utils.cqueue.CQ;
import se.kb.libris.utils.cqueue.Consumer;
import se.kb.libris.utils.cqueue.Producer;
import se.kb.libris.utils.cqueue.Work;
import se.kb.libris.utils.cqueue.Worker;

public class TestQueue {
    public static class MyWorker<SOURCE, RESULT> extends Worker<SOURCE, RESULT> {
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
    }
    
    public static void main(String args[]) throws Exception {
        new CQ<String, String>(
            new Producer<String, String>() {
                int n = 0;
                
                @Override
                public synchronized Work<String, String> getWork() {
                    if (n++ >= 100) return null;
                    return new Work<String, String>(String.valueOf(System.currentTimeMillis()));
                }
            }, new Consumer<String, String>() {
                @Override
                public void consume(Work<String, String> work) {
                    System.out.println(work.seqNo + " " + work.source + " -> " + work.result);
                }
            }, new MyWorker<String, String>()).start().join();
    }
}
