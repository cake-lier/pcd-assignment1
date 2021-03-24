package it.unibo.pcd.assignment1.model.agent;

public interface SharedAgentState {
    void checkForSuspension();

    void setSuspendedState();

    void setRunningState();
}
