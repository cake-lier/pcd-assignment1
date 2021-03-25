package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.Agent;
import it.unibo.pcd.assignment1.model.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeneratorSinkAgent extends Thread implements Agent {
    private final List<Task> tasks;

    public GeneratorSinkAgent(final Collection<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    @Override
    public void run() {
        this.execute();
    }

    @Override
    public void execute() throws Exception {
        for (final Task task : this.tasks) {
            task.run();
        }
    }
}
