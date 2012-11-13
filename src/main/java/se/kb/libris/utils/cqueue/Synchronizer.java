package se.kb.libris.utils.cqueue;

import java.util.HashMap;
import java.util.Map;

public class Synchronizer <SOURCE, RESULT> {
    Long currentSeqNo = Long.valueOf(1);
    Map<Long, Work<SOURCE, RESULT>> buffer = new HashMap<Long, Work<SOURCE, RESULT>>();
    Consumer<SOURCE, RESULT> consumer = null;

    public Synchronizer(Consumer<SOURCE, RESULT> _consumer) {
        consumer = _consumer;
    }
    
    public synchronized void put(Work<SOURCE, RESULT> work) {
        buffer.put(work.seqNo, work);

        while (buffer.containsKey(currentSeqNo)) {
            consumer.consume(buffer.remove(currentSeqNo));
            currentSeqNo += 1;
        }
    }    
}
