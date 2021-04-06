package it.unibo.pcd.assignment1.jpf.controller.agents.impl;

import it.unibo.pcd.assignment1.jpf.controller.agents.Agent;
import it.unibo.pcd.assignment1.jpf.controller.tasks.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AgentImpl extends Thread implements Agent {
    private final List<Task> tasks;

    public AgentImpl(final List<Task> tasks) {
        this.tasks = Objects.requireNonNull(tasks);
    }

    @Override
    public void run() {
        for (final Task task : this.tasks) {
            task.run();
        }
    }

    @Override
    public void go() {
        this.start();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AgentImpl agent = (AgentImpl) o;
        return this.tasks.equals(agent.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tasks);
    }

    @Override
    public String toString() {
        return "AgentImpl{tasks=" + this.tasks+"}";
    }
}
