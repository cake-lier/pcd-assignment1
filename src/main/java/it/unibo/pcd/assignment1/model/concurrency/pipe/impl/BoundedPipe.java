package it.unibo.pcd.assignment1.model.concurrency.pipe.impl;

import java.util.*;
import java.util.concurrent.locks.Condition;

public class BoundedPipe<E> extends UnboundedPipe<E> {
    private final int maxNumberOfElements;
    private final Condition notFull;

    private boolean isFull;

    public BoundedPipe(final int maxNumberOfElements) {
        this.maxNumberOfElements = maxNumberOfElements;
        this.isFull = false;
        this.notFull = this.lock.newCondition();
    }

    protected Optional<E> doDequeue() {
        if(this.isFull){
            this.notFull.signalAll();
            this.isFull = false;
        }
        return super.doDequeue();
    }

    protected void doEnqueue(E value) {
        while(this.isFull){
            try {
                this.notFull.await();
            } catch (InterruptedException ignored) {}
        }
        super.doEnqueue(value);
        if(this.getSize() == this.maxNumberOfElements){
            this.isFull = true;
        }
    }
}
