package it.unibo.pcd.assignment1.model.pipes.impl;

import java.util.*;
import java.util.concurrent.locks.Condition;

public class BoundedPipe<E> extends UnboundedPipe<E> {
    private final int maxElementsNumber;
    private final Condition notFull;
    private boolean isFull;

    public BoundedPipe(final int maxElementsNumber) {
        this.maxElementsNumber = maxElementsNumber;
        this.isFull = false;
        this.notFull = this.lock.newCondition();
    }

    protected Optional<E> doDequeue() {
        if (this.isFull) {
            this.notFull.signalAll();
            this.isFull = false;
        }
        return super.doDequeue();
    }

    protected void doEnqueue(final E value) {
        while (this.isFull) {
            try {
                this.notFull.await();
            } catch (InterruptedException ignored) {}
        }
        super.doEnqueue(value);
        if (this.getSize() == this.maxElementsNumber) {
            this.isFull = true;
        }
    }
}
