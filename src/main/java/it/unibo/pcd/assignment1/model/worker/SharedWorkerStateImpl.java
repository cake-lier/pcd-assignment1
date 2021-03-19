package it.unibo.pcd.assignment1.model.worker;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedWorkerStateImpl implements SharedWorkerState {
    private final Lock lock;
    private final Condition suspendedCondition;
    private boolean isRunning;

    public SharedWorkerStateImpl() {
        this.lock = new ReentrantLock();
        this.suspendedCondition = this.lock.newCondition();
        this.isRunning = true;
    }

    @Override
    public void checkForSuspension() {
        this.lock.lock();
        try {
            while (!this.isRunning) {
                try {
                    this.suspendedCondition.await();
                } catch (InterruptedException ignored) {}
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void setSuspendedState() {
        this.lock.lock();
        try {
            this.isRunning = false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void setRunningState() {
        this.lock.lock();
        try {
            this.isRunning = true;
            this.suspendedCondition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}
