package se.kb.libris.utils.cqueue;

public abstract class Consumer <SOURCE, RESULT> {
    public abstract void consume(Work<SOURCE, RESULT> work);
}
