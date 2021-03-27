package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.Agent;
import it.unibo.pcd.assignment1.controller.agents.Task;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AgentImpl extends Thread implements Agent {
    private final List<Task> tasks;
    private final Consumer<Exception> exceptionHandler;

    public AgentImpl(final List<Task> tasks, final Consumer<Exception> exceptionHandler) {
        this.setName(this.getClass().toString());
        this.tasks = Objects.requireNonNull(tasks);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
    }

    @Override
    public void run() {
        try {
            this.go();
        } catch (final Exception ex) {
            this.exceptionHandler.accept(ex);
        }
    }

    @Override
    public void go() throws Exception {
        for (final Task task : this.tasks) {
            task.run();
        }
    }
}
