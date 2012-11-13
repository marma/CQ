package se.kb.libris.test.cqueue;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQ  {
    public static class Work {
        public Long seqNo;
        public String in = null, out = null;
        
        public Work(long _seqNo, String _in) {
            seqNo = _seqNo;
            in = _in;
        }
    }
    
    public static class Producer {
        public static final long MAX_ITERATIONS = 1000;
        long current = 1;
        long lastRequest = System.currentTimeMillis();
        CQ cq = null;
        
        public void setCQ(CQ _cq) {
            cq = _cq;
        }
        
        public synchronized Work get() throws InterruptedException {
            if (current == MAX_ITERATIONS) return null;
            
            if (System.currentTimeMillis() - lastRequest > 0)
                cq.createThread();
            
            //Thread.sleep((long)(10));
            lastRequest = System.currentTimeMillis();
            return new Work(current++, String.valueOf(current-1));
        }
    }
    
    public static class Synchronizer {
        Long currentSeqNo = Long.valueOf(1);
        Map<Long, Work> buffer = new HashMap<Long, Work>();
        
        private synchronized void put(Work work) {
            buffer.put(work.seqNo, work);
            
            while (buffer.containsKey(currentSeqNo)) {
                Work w = buffer.remove(currentSeqNo);
                System.out.println(w.seqNo + " - " + w.in + " - " + w.out);
                currentSeqNo += 1;
            }
        }
    }

    public static class Worker extends Thread {
        Producer producer = null;
        Synchronizer synchronizer = null;
        
        public Worker(Producer _producer, Synchronizer _synchronizer) {
            producer = _producer;
            synchronizer = _synchronizer;
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    Work w = producer.get();
                    if (w == null) return;
                    Thread.sleep((long)(10*Math.random()));
                    w.out = w.in + "/" + w.in;
                    synchronizer.put(w);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CQ.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    Producer producer = new Producer();
    Synchronizer synchronizer = new Synchronizer();
    public int nThreads = 0;
    
    private synchronized void createThread() {
        if (nThreads < MAX_THREADS) {
            System.out.println("Creating thread " + nThreads + " / " + MAX_THREADS);
            new Worker(producer, synchronizer).start();
            nThreads += 1;
        } else {
            System.out.println("At max threads (" + MAX_THREADS + ")");            
        }
    }

    public CQ() {
        producer.setCQ(this);
        createThread();
    }
    
    public static void main(String[] args) {
        if (args.length == 1)
            CQ.MAX_THREADS = Integer.parseInt(args[0]);
            
        CQ cq = new CQ();
    }
}
