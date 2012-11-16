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
    
    @Override
    public void run() {
        try {
            while (true) {
                Work w = producer.get();

                if (w == null) return;

                try {
                    doWork(w);
                } catch (Exception e) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, e);
                }

                synchronizer.put(w);
            }
        } catch (Exception e) {
        }            
    }

    public abstract Work doWork(Work work) throws Exception;
}
