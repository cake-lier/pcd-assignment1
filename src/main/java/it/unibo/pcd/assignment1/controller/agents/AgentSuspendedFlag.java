package it.unibo.pcd.assignment1.controller.agents;

public interface AgentSuspendedFlag {
    void check();

    void setSuspended();

    void setRunning();
}
