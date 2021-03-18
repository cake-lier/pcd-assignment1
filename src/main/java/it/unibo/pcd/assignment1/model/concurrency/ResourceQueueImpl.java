package it.unibo.pcd.assignment1.model.concurrency;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceQueueImpl<X> implements ResourceQueue<X>{
    private final Queue<X> queue;
    private final Lock lock;
    private final Condition notEmptyCondition;

    public ResourceQueueImpl(){
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmptyCondition = this.lock.newCondition();
    }
    @Override
    public void add(X data) {
        try{
            this.lock.lock();
            queue.add(Objects.requireNonNull(data));
            if(this.isEmpty()){
                this.notEmptyCondition.notifyAll();
            }
        }finally {
            this.lock.unlock();
        }
    }

    @Override
    public Optional<X> pop() {
        try{
            this.lock.lock();
            while (this.isEmpty()){
                try {
                    this.notEmptyCondition.await();
                }catch (InterruptedException e){} //TODO empty catch
            }
            return Optional.of(this.queue.poll());
        }finally {
            this.lock.unlock();
        }
    }

    protected boolean isEmpty(){
        return this.queue.size() == 0;
    }
}
