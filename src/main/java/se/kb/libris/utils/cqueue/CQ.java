package se.kb.libris.utils.cqueue;

import java.lang.reflect.Type;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQ <SOURCE, RESULT>  {
    int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    Producer<SOURCE, RESULT> producer = null;
    Consumer<SOURCE, RESULT> consumer = null;
    Synchronizer<SOURCE, RESULT> synchronizer = null;
    Worker<SOURCE, RESULT> workerPrototype = null;
    Vector<Worker> threads = new Vector<Worker>(MAX_THREADS);
    
    public CQ(Producer<SOURCE, RESULT> _producer, Consumer<SOURCE, RESULT> _consumer, Worker<SOURCE, RESULT> _workerPrototype) {
        producer = _producer;
        producer.setCQ(this);
        consumer = _consumer;
        synchronizer = new Synchronizer<SOURCE, RESULT>(consumer);
        workerPrototype = _workerPrototype;
    }
    
    public CQ(Producer<SOURCE, RESULT> _producer, Consumer<SOURCE, RESULT> _consumer, Worker<SOURCE, RESULT> _workerPrototype, int _maxThreads) {
        this(_producer, _consumer, _workerPrototype);
        MAX_THREADS = _maxThreads;
    }
    
    public synchronized void createThread() {
        if (threads.size() < MAX_THREADS) {
            try {
                System.out.println("Creating thread " + threads.size() + " / " + MAX_THREADS);
                threads.add(workerPrototype.getClass().newInstance().setProducer(producer).setSynchronizer(synchronizer));
                threads.lastElement().start();
            } catch (InstantiationException ex) {
                Logger.getLogger(CQ.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CQ.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("At max thread " + threads.size());
        }
    }
    
    public CQ<SOURCE, RESULT> start() {
        if (threads.isEmpty())
            createThread();
        
        return this;
    }
    
    public void join() throws InterruptedException {
        for (int i=0;i<threads.size();i++)
            threads.elementAt(i).join();
    }
}
