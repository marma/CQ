package se.kb.libris.utils.cqueue;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Producer {
    private static int MAX_WAIT = 1;
    private Long current = 1L;
    private CQ cq = null;
    private long t = System.currentTimeMillis();
    
    public final synchronized Work get() {
        if (System.currentTimeMillis() - t >= MAX_WAIT) cq.createThread();

        Work w = null;

        try {
            w = getWork();
        } catch (Exception ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (w != null)
            w.setSeqNo(current++);
        
        t = System.currentTimeMillis();
        
        return w;
    }
    
    public Producer setCQ(CQ _cq) {
        cq = _cq;
        
        return this;
    }
    
    public abstract Work getWork() throws Exception;
} 
