package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.controller.tasks.Task;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class TaskCounterImpl implements TaskCounter {
    private static final int INCREMENT = 1;
    private static final int DECREMENT = -1;

    private final Map<Class<? extends Task>, Integer> counters;
    private final ReentrantLock lock;

    public TaskCounterImpl() {
        this.counters = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void incrementOfType(final Class<? extends Task> klass) {
        this.lock.lock();
        try {
            this.counters.merge(klass, INCREMENT, (o, n)-> o + n);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean decrementOfType(final Class<? extends Task> klass) {
        this.lock.lock();
        try {
            if (!this.counters.containsKey(klass) || this.counters.get(klass) < 0) {
                throw new IllegalStateException();
            }
            this.counters.merge(klass, DECREMENT, (o, n)-> o + n);
            return this.counters.get(klass) == 0;
        } finally {
            this.lock.unlock();
        }
    }
}
