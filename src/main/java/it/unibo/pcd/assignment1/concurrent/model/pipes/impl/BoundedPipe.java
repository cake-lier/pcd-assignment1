package it.unibo.pcd.assignment1.concurrent.model.pipes.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A "bounded buffer" implementation of the {@link Pipe} interface.
 * @param <E> the type of resource contained into this pipeline
 */
public class BoundedPipe<E> implements Pipe<E> {
    private static final String EXCEPTION_MESSAGE = "It's not possible to add values to a closed pipe";

    private final Queue<E> queue;
    private final Lock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    private final int maxElementsNumber;
    private boolean isClosed;
    private boolean isFull;

    /**
     * Default constructor.
     * @param maxElementsNumber the maximum number of elements this pipe can hold at the same time
     */
    public BoundedPipe(final int maxElementsNumber) {
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        this.maxElementsNumber = maxElementsNumber;
        this.isClosed = false;
        this.isFull = false;
    }

    /**
     * {@inheritDoc}
     * If the queue was full, it wakes up all tasks waiting for enqueueing a new resource.
     */
    @Override
    public final Optional<E> dequeue() {
        this.lock.lock();
        try {
            return this.doDequeue();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     * This operations is blocking if the queue is full.
     */
    @Override
    public final void enqueue(final E value) {
        this.lock.lock();
        try {
            this.doEnqueue(value);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * The actual code to be executed by the dequeue method. This method can be overridden by subclasses so that the dequeue
     * method will still be reentrant but editable for subclasses.
     * @return the next element in the pipeline, if present, an {@link Optional#empty()} otherwise
     */
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

    /**
     * The actual code to be executed by the enqueue method. This method can be overridden by subclasses so that the enqueue
     * method will still be reentrant but editable for subclasses.
     * @param value the value to add to the pipeline
     */
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

    /**
     * It returns the {@link Lock} used for this monitor.
     * @return the {@link Lock} used for this monitor
     */
    protected Lock getLock() {
        return this.lock;
    }

    /**
     * It returns whether or not this pipeline is empty.
     * @return whether or not this pipeline is empty
     */
    protected boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
