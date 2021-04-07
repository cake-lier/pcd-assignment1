package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.DocumentFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PathFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.TaskCounterImpl;

public class PathTest {
    private PathTest() {}

    public static void main(String[] args) {
        final String fileName = "file";
        final int pipesSize = 1000;
        final Pipe<String> pathPipe = new BoundedPipe<>(pipesSize);
        final Pipe<Document> documentPipe = new BoundedPipe<>(pipesSize);
        final TaskCounter taskCounter = new TaskCounterImpl();
        JPFTestUtils.insertDataAndClose(pathPipe, fileName);
        JPFTestUtils.createAgents(new PathFilterTask(pathPipe, documentPipe, taskCounter),
                                  new DocumentFilterTask(documentPipe, new BoundedPipe<>(pipesSize), taskCounter));
    }
}
