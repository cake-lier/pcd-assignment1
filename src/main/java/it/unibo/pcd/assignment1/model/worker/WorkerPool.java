package it.unibo.pcd.assignment1.model.worker;

public interface WorkerPool {
    void start();

    void suspend();

    void resume();
}
