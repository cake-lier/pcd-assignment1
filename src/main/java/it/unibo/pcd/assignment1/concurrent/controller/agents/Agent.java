package it.unibo.pcd.assignment1.concurrent.controller.agents;

/**
 * The active component of this application, capable of executing a sequence of {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task}
 * independently from other components of this same application.
 */
public interface Agent {
    /**
     * This method allows the agent to start its execution.
     */
    void go();
}
