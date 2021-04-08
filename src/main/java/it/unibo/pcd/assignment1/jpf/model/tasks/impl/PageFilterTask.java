package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.Update;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.UpdateImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.*;

public class PageFilterTask extends AbstractSingletonFilterTask<Page, Update> {

    public PageFilterTask(final Pipe<Page> pagePipe,
                             final Pipe<Update> updatePipe,
                             final TaskCounter taskCounter) {
        super(pagePipe, updatePipe, taskCounter);
    }

    @Override
    protected Update transformSingleton() {
        return new UpdateImpl(Collections.singletonMap("word", 1L),2);
    }
}