package it.unibo.pcd.assignment1.concurrent.controller.agents.impl;

import it.unibo.pcd.assignment1.concurrent.controller.agents.Agent;
import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An implementation of the {@link Agent} interface using the Java {@link Thread} class.
 */
public class AgentImpl extends Thread implements Agent {
    private final List<Task> tasks;
    private final Consumer<Exception> exceptionHandler;

    public AgentImpl(final List<Task> tasks, final Consumer<Exception> exceptionHandler) {
        super(tasks.get(0).toString());
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
    public String toString() {
        return "AgentImpl{tasks=" + this.tasks + ", exceptionHandler=" + this.exceptionHandler + '}';
    }
}
