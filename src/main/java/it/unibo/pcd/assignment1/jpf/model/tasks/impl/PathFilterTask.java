package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Collections;
import java.util.List;

public class PathFilterTask extends AbstractSingletonFilterTask<String, Document> {
    private final List<Page> pages;

    public PathFilterTask(final Pipe<String> pathPipe, final Pipe<Document> documentPipe, final TaskCounter taskCounter) {
        super(pathPipe, documentPipe, taskCounter);
        this.pages = Collections.singletonList(new PageImpl());
    }

    @Override
    protected Document transformSingleton(){
        return new DocumentImpl(this.pages);
    }
}
