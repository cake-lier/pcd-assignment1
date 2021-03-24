package it.unibo.pcd.assignment1.model.concurrency;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedPipe<E> implements Pipe<E> {
    private static final String EXCEPTION_MESSAGE = "It's not possible to add values to a closed pipe";

    private final Lock lock;
    private final Queue<E> queue;
    private final Condition notEmpty;
    private final Condition notFull;
    private final int maxNumberOfElements;

    private boolean isClosed;
    private boolean isFull;

    public BoundedPipe(final int maxNumberOfElements) {
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.isClosed = false;
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        this.maxNumberOfElements = maxNumberOfElements;
        this.isFull = false;
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

    @Override
    public final void enqueue(E value) {
        try {
            this.lock.lock();
            this.doEnqueue(value);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void close() {
        try {
            this.lock.lock();
            this.isClosed = true;
            if (this.isEmpty()) {
                this.notEmpty.notifyAll();
            }
        } finally {
            this.lock.unlock();
        }
    }

    protected Optional<E> doDequeue() {
        while (this.isEmpty() && !this.isClosed) {
            try {
                this.notEmpty.await();
            } catch (InterruptedException ignored) {}
        }
        if(!this.isClosed && this.isFull){
            this.notFull.signalAll();
            this.isFull = false;
        }
        return this.isClosed ? Optional.empty() : Optional.ofNullable(this.queue.poll());
    }

    protected void doEnqueue(E value) {
        if (this.isClosed) {
            throw new IllegalStateException(EXCEPTION_MESSAGE);
        }else{
            while(this.isFull){
                try {
                    this.notFull.await();
                } catch (InterruptedException ignored) {}
            }
            if (this.isEmpty()) {
                this.notEmpty.signalAll();
            }
            this.queue.add(Objects.requireNonNull(value));
            if(this.queue.size() > this.maxNumberOfElements){
                this.isFull = true;
            }
        }
    }

    protected boolean isEmpty(){ return this.queue.isEmpty(); }
}
