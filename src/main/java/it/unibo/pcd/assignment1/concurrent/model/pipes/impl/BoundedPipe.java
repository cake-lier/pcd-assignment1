package it.unibo.pcd.assignment1.concurrent.model.pipes.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedPipe<E> implements Pipe<E> {
    private static final String EXCEPTION_MESSAGE = "It's not possible to add values to a closed pipe";

    private final Queue<E> queue;
    protected final Lock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    private final int maxElementsNumber;
    private boolean isClosed;
    private boolean isFull;

    public BoundedPipe(final int maxElementsNumber) {
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        this.maxElementsNumber = maxElementsNumber;
        this.isClosed = false;
        this.isFull = false;
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
        if (this.isFull) {
            this.notFull.signalAll();
            this.isFull = false;
        }
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
        while (this.isFull) {
            try {
                this.notFull.await();
            } catch (InterruptedException ignored) {}
        }
        if (this.queue.isEmpty()) {
            this.notEmpty.signalAll();
        }
        this.queue.add(Objects.requireNonNull(value));
        if (this.queue.size() == this.maxElementsNumber) {
            this.isFull = true;
        }
    }

    protected Lock getLock() {
        return this.lock;
    }

    protected boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
