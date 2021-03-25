package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.Agent;
import it.unibo.pcd.assignment1.model.tasks.Task;
import it.unibo.pcd.assignment1.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GeneratorAgent extends Thread implements Agent {
    private final List<Task> tasks;
    private final View view;

    public GeneratorAgent(final Collection<Task> tasks, final View view) {
        this.tasks = new ArrayList<>(tasks);
        this.view = Objects.requireNonNull(view);
    }

    @Override
    public void run() {
        try {
            this.go();
        } catch (final Exception ex) {
            this.view.displayError(ex.getMessage());
        }
    }

    @Override
    public void go() throws Exception {
        for (final Task task : this.tasks) {
            task.run();
        }
    }
}
