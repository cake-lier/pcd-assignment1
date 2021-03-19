package it.unibo.pcd.assignment1.model.concurrency;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceQueueImpl<E> implements ResourceQueue<E> {

    private final Lock lock;
    private final Condition notEmptyCondition;
    protected final Queue<E> queue;

    public ResourceQueueImpl(final Collection<E> initialElements){
        this.queue = new LinkedList<>(initialElements);
        this.lock = new ReentrantLock();
        this.notEmptyCondition = this.lock.newCondition();
    }
    @Override
    public void enqueue(final E value) {
        try{
            this.lock.lock();
            this.doEnqueue(Objects.requireNonNull(value));
        }finally {
            this.lock.unlock();
        }
    }

    @Override
    public Optional<E> dequeue() {
        try{
            this.lock.lock();
            return this.doDequeue();
        }finally {
            this.lock.unlock();
        }
    }

    protected boolean isEmpty(){
        return this.queue.size() == 0;
    }

    protected Optional<E> doDequeue() {
        while (this.isEmpty()){
            try {
                this.notEmptyCondition.await();
            }catch (InterruptedException e){} //TODO empty catch
        }
        if(this.queue.isEmpty()){
            throw new IllegalStateException();
        }
        return Optional.of(this.queue.poll());
    }

    protected void doEnqueue(E value) {
        this.queue.add(value);
        if(this.isEmpty()){
            this.notEmptyCondition.notifyAll();
        }
    }
}
