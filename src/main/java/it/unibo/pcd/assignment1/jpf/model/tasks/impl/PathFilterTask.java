package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PathFilterTask extends AbstractSingletonFilterTask<String, Document> {
    private final int pagesForDocument;
    private final int wordsForPage;

    public PathFilterTask(final Pipe<String> pathPipe, final Pipe<Document> documentPipe, final TaskCounter taskCounter, int pagesForDocument, int wordsForPage) {
        super(pathPipe, documentPipe, taskCounter);
        this.pagesForDocument = pagesForDocument;
        this.wordsForPage = wordsForPage;
    }

    @Override
    protected Document transformSingleton(final String path){
        final List<Page> pageList = new ArrayList<>();
        StringBuilder wordsList = new StringBuilder();
        IntStream.range(0,this.wordsForPage).forEach(i->wordsList.append("word "));
        final String text = wordsList.toString();
        IntStream.range(0,this.pagesForDocument).forEach(i->pageList.add(new PageImpl(text)));
        return new DocumentImpl(pageList);
    }
}
