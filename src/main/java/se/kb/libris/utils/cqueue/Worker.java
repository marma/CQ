package se.kb.libris.utils.cqueue;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Worker<SOURCE, RESULT> extends Thread {
    Producer producer = null;
    Synchronizer synchronizer = null;

    public Worker<SOURCE, RESULT> setProducer(Producer<SOURCE, RESULT> _producer) {
        producer = _producer;
        
        return this;
    }
    
    public Worker<SOURCE, RESULT> setSynchronizer(Synchronizer<SOURCE, RESULT> _synchronizer) {
        synchronizer = _synchronizer;
        
        return this;
    }
    
    public abstract Work<SOURCE, RESULT> doWork(Work<SOURCE, RESULT> work);

    @Override
    public void run() {
        while (true) {
            Work<SOURCE, RESULT> w = producer.get();
            
            if (w == null) return;

            synchronizer.put(doWork(w));
        }
    }
}
