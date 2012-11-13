package se.kb.libris.utils.cqueue;

import java.lang.reflect.Type;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQ  {
    int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    Producer producer = null;
    Consumer consumer = null;
    Synchronizer synchronizer = null;
    Worker workerPrototype = null;
    Vector<Worker> threads = new Vector<Worker>(MAX_THREADS);
    
    public CQ(Producer _producer, Consumer _consumer, Worker _workerPrototype) {
        producer = _producer;
        producer.setCQ(this);
        consumer = _consumer;
        synchronizer = new Synchronizer(consumer);
        workerPrototype = _workerPrototype;
    }
    
    public CQ(Producer _producer, Consumer _consumer, Worker _workerPrototype, int _maxThreads) {
        this(_producer, _consumer, _workerPrototype);
        MAX_THREADS = _maxThreads;
    }
    
    public synchronized void createThread() {
        if (threads.size() < MAX_THREADS) {
            try {
                //System.out.println("Creating thread " + threads.size() + " / " + MAX_THREADS);
                threads.add(workerPrototype.getClass().newInstance().setProducer(producer).setSynchronizer(synchronizer));
                threads.lastElement().start();
            } catch (InstantiationException ex) {
                Logger.getLogger(CQ.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CQ.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //System.out.println("At max thread " + threads.size());
        }
    }
    
    public CQ start() {
        if (threads.isEmpty())
            createThread();
        
        return this;
    }
    
    public void join() throws InterruptedException {
        for (int i=0;i<threads.size();i++) {
            threads.elementAt(i).join();
        }
    }
}
