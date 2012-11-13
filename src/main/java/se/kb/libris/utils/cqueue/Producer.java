package se.kb.libris.utils.cqueue;

public abstract class Producer<SOURCE, RESULT> {
    private static int MAX_WAIT = 1;
    private Long current = 1L;
    private CQ<SOURCE, RESULT> cq = null;
    private long t = System.currentTimeMillis();
    
    public synchronized Work<SOURCE, RESULT> get() {
        if (System.currentTimeMillis() - t > MAX_WAIT) cq.createThread();
        Work<SOURCE, RESULT> w = getWork();
        
        if (w != null)
            w.setSeqNo(current++);
        
        t = System.currentTimeMillis();
        
        return w;
    }
    
    public Producer<SOURCE, RESULT> setCQ(CQ<SOURCE, RESULT> _cq) {
        cq = _cq;
        
        return this;
    }
    
    public abstract Work<SOURCE, RESULT> getWork();
} 
