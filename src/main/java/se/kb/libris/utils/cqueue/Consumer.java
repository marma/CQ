package se.kb.libris.utils.cqueue;

public abstract class Consumer {
    public abstract void consume(Work work) throws Exception;
}
