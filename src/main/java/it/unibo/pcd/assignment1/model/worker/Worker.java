package it.unibo.pcd.assignment1.model.worker;

public interface Worker {
    void start();

    boolean hasCompleted();

    void await();
}
