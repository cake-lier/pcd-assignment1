package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.WordCounterImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.DocumentFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PageFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.TaskCounterImpl;

import java.util.Collections;

public class DocumentTest {
    private DocumentTest() {}

    public static void main(String[] args) {
        final int pipesSize = 1000;
        final Pipe<Page> pagePipe = new BoundedPipe<>(pipesSize);
        final Pipe<Document> documentPipe = new BoundedPipe<>(pipesSize);
        final TaskCounter taskCounter = new TaskCounterImpl();
        JPFTestUtils.insertDataAndClose(documentPipe, new DocumentImpl(Collections.singletonList(new PageImpl())));
        JPFTestUtils.createAgents(new DocumentFilterTask(documentPipe, pagePipe, taskCounter),
                                  new PageFilterTask(pagePipe, new WordCounterImpl(pipesSize), taskCounter));
    }
}
