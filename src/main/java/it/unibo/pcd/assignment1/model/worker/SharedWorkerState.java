package it.unibo.pcd.assignment1.model.worker;

public interface SharedWorkerState {
    void checkForSuspension();

    void setSuspendedState();

    void setRunningState();
}
