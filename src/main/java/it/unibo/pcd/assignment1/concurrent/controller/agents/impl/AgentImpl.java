package it.unibo.pcd.assignment1.concurrent.controller.agents.impl;

import it.unibo.pcd.assignment1.concurrent.controller.agents.Agent;
import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AgentImpl extends Thread implements Agent {
    private final List<Task> tasks;
    private final Consumer<Exception> exceptionHandler;

    public AgentImpl(final List<Task> tasks, final Consumer<Exception> exceptionHandler) {
        this.tasks = Objects.requireNonNull(tasks);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
    }

    @Override
    public void run() {
        try {
            for (final Task task : this.tasks) {
                task.run();
            }
        } catch (final Exception ex) {
            this.exceptionHandler.accept(ex);
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
        return this.tasks.equals(agent.tasks) && this.exceptionHandler.equals(agent.exceptionHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tasks, this.exceptionHandler);
    }

    @Override
    public String toString() {
        return "AgentImpl{tasks=" + this.tasks + ", exceptionHandler=" + this.exceptionHandler + '}';
    }
}
