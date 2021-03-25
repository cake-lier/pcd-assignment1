package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.Agent;
import it.unibo.pcd.assignment1.view.View;

import java.util.Objects;

public class FilterAgent extends Thread implements Agent {
    private final TaskIterator tasks;
    private final View view;

    public FilterAgent(final TaskIterator tasks, final View view){
        this.tasks = Objects.requireNonNull(tasks);
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
        while(this.tasks.hasNext()) {
            this.tasks.next().run();
        }
    }
}
