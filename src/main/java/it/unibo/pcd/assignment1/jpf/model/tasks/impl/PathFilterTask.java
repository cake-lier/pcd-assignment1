package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PathFilterTask extends AbstractSingletonFilterTask<String, Document> {

    private final List<Page> pages;

    public PathFilterTask(final Pipe<String> pathPipe,
                             final Pipe<Document> documentPipe,
                             final TaskCounter taskCounter) {
        super(pathPipe, documentPipe, taskCounter);
        this.pages = Collections.singletonList(new PageImpl("page1"));
    }

    @Override
    protected Document transformSingleton(final String path){
        return new DocumentImpl(pages);
    }
}
