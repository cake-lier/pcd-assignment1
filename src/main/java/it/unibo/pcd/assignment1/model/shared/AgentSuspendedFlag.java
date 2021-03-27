package it.unibo.pcd.assignment1.model.shared;

public interface AgentSuspendedFlag {
    void check();

    void setSuspended();

    void setRunning();
}
