package se.kb.libris.utils.cqueue;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQ  {
    public static int OK = 0x1;
    public static int ERROR = 0x2;
    int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    int MIN_THREADS = 1;
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
    
    public CQ(Producer _producer, Consumer _consumer, WorkerFactory _workerFactory, int _maxThreads, int _minThreads) {
        this(_producer, _consumer, _workerFactory);
        MAX_THREADS = _maxThreads;
        MIN_THREADS = _minThreads;
    }
    
    public synchronized void createThread() {
        if (threads.size() < MAX_THREADS) {
            //System.out.println("Creating thread " + threads.size() + " / " + MAX_THREADS);
            threads.add(workerFactory.createWorker().setProducer(producer).setSynchronizer(synchronizer));
            threads.lastElement().start();
        } else {
            //System.out.println("At max thread " + threads.size());
        }
    }
    
    public CQ start() {
        if (threads.isEmpty())
            for (int i=0;i<MIN_THREADS;i++)
                createThread();
        
        return this;
    }
    
    public void join() {
        for (int i=0;i<threads.size();i++)
            try {
                threads.elementAt(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
