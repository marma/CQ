package se.kb.libris.utils.cqueue;

public class Work {
    public Long seqNo;
    public Object source = null;
    public Object result = null;
        
    public Work(Object _source) {
        source = _source;
    }
    
    public Work setSeqNo(Long _seqNo) {
        seqNo = _seqNo;

        return this;
    }
}
