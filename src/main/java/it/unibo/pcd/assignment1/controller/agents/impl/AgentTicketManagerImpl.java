package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class AgentTicketManagerImpl implements AgentTicketManager {
    private static final int INCREMENT = 1;
    private static final int DECREMENT = -1;

    private final Map<Class<?>,Integer> counters;
    private final ReentrantLock lock;

    public AgentTicketManagerImpl() {
        this.counters = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void incrementAgentOfType(Class<?> klass) {
        this.lock.lock();
        try {
            this.counters.merge(klass,INCREMENT, Integer::sum);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean decrementAgentOfType(Class<?> klass) {
        this.lock.lock();
        try{
            if(!this.counters.containsKey(klass) || this.counters.get(klass) < 0){
                throw new IllegalStateException();
            }
            this.counters.merge(klass,DECREMENT, Integer::sum);
            return this.counters.get(klass) == 0;
        } finally {
            this.lock.unlock();
        }
    }
}
