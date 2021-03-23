package it.unibo.pcd.assignment1.model.concurrency;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Condition;

public class FilterPipeImpl<E> extends GeneratorPipeImpl<E> implements FilterPipe<E> {
    private static final String EXCEPTION_MESSAGE = "It's not possible to add values to a closed pipe";

    private final Condition notEmpty;
    private boolean isClosed;

    public FilterPipeImpl() {
        super();
        this.notEmpty = this.getLock().newCondition();
        this.isClosed = false;
    }

    @Override
    public final void enqueue(final E element) {
        try {
            this.getLock().lock();
            this.doEnqueue(element);
        } finally {
            this.getLock().unlock();
        }
    }

    @Override
    public final void close() {
        try {
            this.getLock().lock();
            this.isClosed = true;
            if (this.queue.isEmpty()) {
                this.notEmpty.notifyAll();
            }
        } finally {
            this.getLock().unlock();
        }
    }

    @Override
    protected Optional<E> doDequeue() {
        while (this.isEmpty() && !this.isClosed) {
            try {
                this.notEmpty.await();
            } catch (InterruptedException ignored) {}
        }
        return super.doDequeue();
    }

    protected void doEnqueue(final E element) {
        if (this.isClosed) {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        this.queue.add(Objects.requireNonNull(element));
        if (this.queue.isEmpty()) {
            this.notEmpty.notifyAll();
        }
    }
}

