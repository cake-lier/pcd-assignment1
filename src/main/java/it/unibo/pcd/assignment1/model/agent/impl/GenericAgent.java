package it.unibo.pcd.assignment1.model.agent.impl;

import it.unibo.pcd.assignment1.model.agent.Agent;
import it.unibo.pcd.assignment1.model.policy.Policy;

import java.util.List;

public class GenericAgent extends Thread implements Agent {
    private final List<Policy> policies;

    public GenericAgent(final List<Policy> policies){
        this.policies = policies;
    }

    @Override
    public synchronized void run() {
        this.policies.forEach(Policy::start);
    }

    @Override
    public void go() {
        super.start();
    }
}
