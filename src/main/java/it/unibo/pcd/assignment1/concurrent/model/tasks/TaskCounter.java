package it.unibo.pcd.assignment1.concurrent.model.tasks;

import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;

public interface TaskCounter {
    void incrementOfType(Class<? extends Task> klass);

    boolean decrementOfType(Class<? extends Task> klass);
}
