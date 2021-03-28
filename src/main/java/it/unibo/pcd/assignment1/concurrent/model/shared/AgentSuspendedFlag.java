package it.unibo.pcd.assignment1.concurrent.model.shared;

public interface AgentSuspendedFlag {
    void check();

    void setSuspended();

    void setRunning();
}
