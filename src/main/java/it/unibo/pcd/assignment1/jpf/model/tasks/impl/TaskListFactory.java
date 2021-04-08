package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.controller.tasks.Task;
import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.Update;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class TaskListFactory {
    private final Pipe<String> paths;
    private final Pipe<Document> documents;
    private final Pipe<Page> pages;
    private final Pipe<Update> updates;
    private final TaskCounter taskCounter;
    private final int documentNumber;
    private final int wordsForPage;
    private final int pagesForDocument;

    public TaskListFactory(final int documentNumber,
                           final int pagesForDocument,
                           final int wordsForPage,
                           final int pipesSize,
                           final Pipe<Update> updates,
                           final TaskCounter taskCounter) {
        this.paths = new BoundedPipe<>(pipesSize);
        this.documents = new BoundedPipe<>(pipesSize);
        this.pages = new BoundedPipe<>(pipesSize);
        this.updates = Objects.requireNonNull(updates);
        this.taskCounter = Objects.requireNonNull(taskCounter);
        this.wordsForPage = wordsForPage;
        this.documentNumber = documentNumber;
        this.pagesForDocument = pagesForDocument;
    }

    @SuppressWarnings("Convert2MethodRef")
    public List<Task> createForFilterAgent(final FilterTaskTypes firstTaskType) {
        final List<Task> agents = new ArrayList<>();
        Arrays.<Supplier<Task>>asList(() -> new PathFilterTask(this.paths, this.documents,this.taskCounter, pagesForDocument, wordsForPage),
                                () -> new DocumentFilterTask(this.documents, this.pages, this.taskCounter),
                                () -> new PageFilterTask(this.pages,
                                                         this.updates,
                                                         this.taskCounter))
                     .subList(firstTaskType.ordinal(), FilterTaskTypes.values().length)
                     .stream()
                     .map(e->e.get())
                     .forEach(e->agents.add(e));
        return agents;
    }

    public List<Task> createForGeneratorAgent() {
        final List<Task> taskList = new ArrayList<>(this.createForFilterAgent(FilterTaskTypes.PATH));
        taskList.add(0, new PathGeneratorTask(this.documentNumber,this.paths,this.taskCounter));
        return taskList;
    }
}
