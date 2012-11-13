package se.kb.libris.utils.cqueue;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Worker extends Thread {
    Producer producer = null;
    Synchronizer synchronizer = null;

    public Worker setProducer(Producer _producer) {
        producer = _producer;
        
        return this;
    }
    
    public Worker setSynchronizer(Synchronizer _synchronizer) {
        synchronizer = _synchronizer;
        
        return this;
    }
    
    public abstract Work doWork(Work work);

    @Override
    public void run() {
        while (true) {
            Work w = producer.get();
            
            if (w == null) return;

            synchronizer.put(doWork(w));
        }
    }
}
