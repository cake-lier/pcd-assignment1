package it.unibo.pcd.assignment1.model.concurrency;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GeneratorPipeImpl<E> implements GeneratorPipe<E> {
    private final Lock lock;
    protected final Queue<E> queue;

    public GeneratorPipeImpl(final Collection<E> elements) {
        this.queue = new LinkedList<>(elements);
        this.lock = new ReentrantLock();
    }

    protected GeneratorPipeImpl() {
        this(Collections.emptyList());
    }

    @Override
    public final Optional<E> dequeue() {
        try {
            this.lock.lock();
            return this.doDequeue();
        } finally {
            this.lock.unlock();
        }
    }

    protected final boolean isEmpty() {
        return this.queue.isEmpty();
    }

    protected final Lock getLock(){
        return this.lock;
    }

    protected Optional<E> doDequeue() {
        return Optional.ofNullable(this.queue.poll());
    }
}
