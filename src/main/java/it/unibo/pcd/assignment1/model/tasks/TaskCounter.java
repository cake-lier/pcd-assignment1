package it.unibo.pcd.assignment1.model.tasks;

import it.unibo.pcd.assignment1.controller.agents.Task;

public interface TaskCounter {
    void incrementOfType(Class<? extends Task> klass);

    boolean decrementOfType(Class<? extends Task> klass);
}
