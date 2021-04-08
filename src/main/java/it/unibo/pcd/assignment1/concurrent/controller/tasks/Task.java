package it.unibo.pcd.assignment1.concurrent.controller.tasks;

/**
 * A task that can be executed by an {@link it.unibo.pcd.assignment1.concurrent.controller.agents.Agent} as part of its behaviour.
 */
public interface Task {
    /**
     * It runs the task, executing its code.
     * @throws Exception an exception that the task might throw during its execution
     */
    void run() throws Exception;
}
