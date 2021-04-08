package it.unibo.pcd.assignment1.concurrent.model.tasks;

import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;

/**
 * A counter which can keep the number of each type of {@link Task} currently running into the system.
 */
public interface TaskCounter {
    /**
     * It increments the counter for the given type of {@link Task}.
     * @param klass the class object which represent the type of the {@link Task} to increment
     */
    void incrementOfType(Class<? extends Task> klass);

    /**
     * It decrements the counter for the given type of {@link Task}.
     * @param klass the class object which represent the type of the {@link Task} to decrement
     * @return if this {@link Task} was the last of its type, or, equivalently, if the counter reached zero
     */
    boolean decrementOfType(Class<? extends Task> klass);
}
