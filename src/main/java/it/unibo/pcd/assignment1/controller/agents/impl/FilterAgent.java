package it.unibo.pcd.assignment1.controller.agents.impl;

import it.unibo.pcd.assignment1.controller.agents.Agent;
import it.unibo.pcd.assignment1.model.tasks.Task;

import java.util.Objects;

public class FilterAgent extends Thread implements Agent {
    private final TaskIterator tasks;

    public FilterAgent(final TaskIterator tasks){
        this.tasks = Objects.requireNonNull(tasks);
    }

    @Override
    public void run() {
        this.execute();
    }

    @Override
    public void execute() throws Exception {
        while(this.tasks.hasNext()) {
            this.tasks.next().run();
        }
    }
}
