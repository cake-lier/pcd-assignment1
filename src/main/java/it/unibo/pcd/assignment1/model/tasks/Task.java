package it.unibo.pcd.assignment1.model.tasks;

public interface Task {
    void run() throws Exception;
    void onEnd() throws Exception;
}
