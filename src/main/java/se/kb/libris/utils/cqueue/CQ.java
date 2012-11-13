package se.kb.libris.utils.cqueue;

public class CQ <SOURCE, RESULT, WORKER extends Thread>  {
    int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    
    public class Work<SOURCE, RESULT> {
        public Long seqNo;
        public SOURCE source = null;
        public RESULT result = null;
        
        public Work(Long _seqNo, SOURCE _source) {
            seqNo = _seqNo;
            source = _source;
        }
    }
    
    public class Producer<IN> {
        
    } 
}
