package it.unibo.pcd.assignment1.jpf.main;

import it.unibo.pcd.assignment1.jpf.controller.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.WordCounterImpl;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.PageFilterTask;
import it.unibo.pcd.assignment1.jpf.model.tasks.impl.TaskCounterImpl;

public class PageTest {
    private PageTest() {}

    public static void main(final String[] args) {
        final int pipesSize = 1000;
        final Pipe<Page> pagePipe = new BoundedPipe<>(pipesSize);
        final WordCounter wordCounter = new WordCounterImpl(pipesSize);
        final TaskCounter taskCounter = new TaskCounterImpl();
        JPFTestUtils.insertDataAndClose(pagePipe, new PageImpl());
        JPFTestUtils.createAgents(new PageFilterTask(pagePipe, wordCounter, taskCounter),
                                  new UpdateSinkTask(wordCounter, taskCounter));
    }
}
