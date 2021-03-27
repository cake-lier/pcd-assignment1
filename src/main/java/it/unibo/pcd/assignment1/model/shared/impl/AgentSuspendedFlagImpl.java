package it.unibo.pcd.assignment1.model.shared.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AgentSuspendedFlagImpl implements AgentSuspendedFlag {
    private final Lock lock;
    private final Condition suspendedCondition;
    private boolean isRunning;

    public AgentSuspendedFlagImpl() {
        this.lock = new ReentrantLock();
        this.suspendedCondition = this.lock.newCondition();
        this.isRunning = true;
    }

    @Override
    public void check() {
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
    public void setSuspended() {
        this.lock.lock();
        try {
            this.isRunning = false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void setRunning() {
        this.lock.lock();
        try {
            this.isRunning = true;
            this.suspendedCondition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}
