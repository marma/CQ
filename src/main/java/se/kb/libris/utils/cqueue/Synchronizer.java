package se.kb.libris.utils.cqueue;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Synchronizer {
    Long currentSeqNo = 1L;
    Map<Long, Work> buffer = new HashMap<Long, Work>();
    Consumer consumer = null;
    Exception e = null;

    public Synchronizer(Consumer _consumer) {
        consumer = _consumer;
    }
    
    public synchronized void put(Work work) throws Exception {
        if (e != null) throw e; 
        
        buffer.put(work.seqNo, work);

        while (buffer.containsKey(currentSeqNo)) {
            try {
                consumer.consume(buffer.remove(currentSeqNo));
            } catch (Exception e2) {
                e = e2;
                Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, e);
                throw e;
            }
            
            currentSeqNo += 1;
        }
    }    
}
