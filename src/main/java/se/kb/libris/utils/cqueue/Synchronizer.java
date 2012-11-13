package se.kb.libris.utils.cqueue;

import java.util.HashMap;
import java.util.Map;

public class Synchronizer <SOURCE, RESULT> {
    Long currentSeqNo = Long.valueOf(1);
    Map<Long, Work> buffer = new HashMap<Long, Work>();
    Consumer consumer = null;

    public Synchronizer(Consumer _consumer) {
        consumer = _consumer;
    }
    
    public synchronized void put(Work work) {
        buffer.put(work.seqNo, work);

        while (buffer.containsKey(currentSeqNo)) {
            consumer.consume(buffer.remove(currentSeqNo));
            currentSeqNo += 1;
        }
    }    
}
