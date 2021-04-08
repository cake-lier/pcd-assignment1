package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class PathFilterTask extends AbstractSingletonFilterTask<String, Document> {
    private final int pagesForDocument;
    private final int wordsForPage;
    private final String word;

    public PathFilterTask(final Pipe<String> pathPipe,
                          final Pipe<Document> documentPipe,
                          final TaskCounter taskCounter,
                          final int pagesForDocument,
                          final int wordsForPage,
                          final String word) {
        super(pathPipe, documentPipe, taskCounter);
        this.pagesForDocument = pagesForDocument;
        this.wordsForPage = wordsForPage;
        this.word = Objects.requireNonNull(word);
    }

    @Override
    protected Document transformSingleton(final String path) {
        final List<Page> pageList = new ArrayList<>();
        final StringBuilder wordsList = new StringBuilder();
        IntStream.range(0, this.wordsForPage).forEach(i -> wordsList.append(this.word).append(" "));
        final String text = wordsList.toString();
        IntStream.range(0, this.pagesForDocument).forEach(i -> pageList.add(new PageImpl(text)));
        return new DocumentImpl(pageList);
    }
}
