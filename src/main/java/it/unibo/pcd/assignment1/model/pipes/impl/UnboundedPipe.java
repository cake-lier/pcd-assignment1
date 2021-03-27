package it.unibo.pcd.assignment1.model.pipes.impl;

import it.unibo.pcd.assignment1.model.pipes.Pipe;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO: delete when sure
public class UnboundedPipe<E> implements Pipe<E> {
    private static final String EXCEPTION_MESSAGE = "It's not possible to add values to a closed pipe";

    protected final Lock lock;
    private final Queue<E> queue;
    private final Condition notEmpty;
    private boolean isClosed;

    public UnboundedPipe() {
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.isClosed = false;
        this.notEmpty = this.lock.newCondition();
    }

    @Override
    public final Optional<E> dequeue() {
        this.lock.lock();
        try {
            return this.doDequeue();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public final void enqueue(final E value) {
        this.lock.lock();
        try {
            this.doEnqueue(value);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public final void close() {
        this.lock.lock();
        try {
            this.isClosed = true;
            if (this.queue.isEmpty()) {
                this.notEmpty.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }

    protected Optional<E> doDequeue() {
        while (this.queue.isEmpty() && !this.isClosed) {
            try {
                this.notEmpty.await();
            } catch (InterruptedException ignored) {}
        }
        return Optional.ofNullable(this.queue.poll());
    }

    protected void doEnqueue(final E value) {
        if (this.isClosed) {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }
        if (this.queue.isEmpty()) {
            this.notEmpty.signalAll();
        }
        this.queue.add(Objects.requireNonNull(value));
    }

    protected int getSize() {
        return this.queue.size();
    }

    protected Lock getLock() {
        return this.lock;
    }
}
