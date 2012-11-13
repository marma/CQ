package se.kb.libris.utils.cqueue;

public class Work<SOURCE, RESULT> {
    public Long seqNo;
    public SOURCE source = null;
    public RESULT result = null;
        
    public Work(SOURCE _source) {
        source = _source;
    }
    
    public Work setSeqNo(Long _seqNo) {
        seqNo = _seqNo;

        return this;
    }
}
