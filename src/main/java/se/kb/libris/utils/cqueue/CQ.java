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
    WorkerFactory workerFactory = null;
    Vector<Worker> threads = new Vector<Worker>(MAX_THREADS);
    
    public CQ(Producer _producer, Consumer _consumer, WorkerFactory _workerFactory) {
        producer = _producer;
        producer.setCQ(this);
        consumer = _consumer;
        synchronizer = new Synchronizer(consumer);
        workerFactory = _workerFactory;
    }
    
    public CQ(Producer _producer, Consumer _consumer, WorkerFactory _workerFactory, int _maxThreads) {
        this(_producer, _consumer, _workerFactory);
        MAX_THREADS = _maxThreads;
    }
    
    public synchronized void createThread() {
        if (threads.size() < MAX_THREADS) {
            System.out.println("Creating thread " + threads.size() + " / " + MAX_THREADS);
            threads.add(workerFactory.createWorker().setProducer(producer).setSynchronizer(synchronizer));
            threads.lastElement().start();
        } else {
            System.out.println("At max thread " + threads.size());
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
