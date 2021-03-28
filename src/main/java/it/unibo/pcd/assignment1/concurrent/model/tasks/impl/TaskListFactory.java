package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;
import it.unibo.pcd.assignment1.concurrent.model.entities.Document;
import it.unibo.pcd.assignment1.concurrent.model.entities.Page;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.concurrent.model.shared.impl.StopwordsSetImpl;
import it.unibo.pcd.assignment1.concurrent.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.entities.Update;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TaskListFactory {
    private final Pipe<Path> paths;
    private final Pipe<Document> documents;
    private final Pipe<Page> pages;
    private final Pipe<Update> updates;
    private final AgentSuspendedFlag suspendedFlag;
    private final TaskCounter taskCounter;
    private final StopwordsSet stopwords;

    public TaskListFactory(final int pipesSize,
                           final Pipe<Update> updates,
                           final AgentSuspendedFlag suspendedFlag,
                           final TaskCounter taskCounter) {
        this.paths = new BoundedPipe<>(pipesSize);
        this.documents = new BoundedPipe<>(pipesSize);
        this.pages = new BoundedPipe<>(pipesSize);
        this.updates = Objects.requireNonNull(updates);
        this.suspendedFlag = Objects.requireNonNull(suspendedFlag);
        this.taskCounter = Objects.requireNonNull(taskCounter);
        this.stopwords = new StopwordsSetImpl();
    }

    public List<Task> createForFilterAgent(final FilterTaskTypes firstTaskType) {
        return Arrays.<Supplier<Task>>asList(() -> new PathFilterTask(this.paths, this.documents, this.suspendedFlag, this.taskCounter),
                                () -> new DocumentFilterTask(this.documents, this.pages, this.suspendedFlag, this.taskCounter),
                                () -> new PageFilterTask(this.pages,
                                                         this.updates,
                                                         this.stopwords,
                                                         this.suspendedFlag,
                                                         this.taskCounter))
                     .subList(firstTaskType.ordinal(), FilterTaskTypes.values().length)
                     .stream()
                     .map(Supplier::get)
                     .collect(Collectors.toList());
    }

    public List<Task> createForGeneratorAgent(final Path filesDirectory, final Path stopwordsFile) {
        final List<Task> taskList = new ArrayList<>(this.createForFilterAgent(FilterTaskTypes.PATH));
        taskList.add(0, new PathGeneratorTask(filesDirectory,
                                                 stopwordsFile,
                                                 this.paths,
                                                 this.suspendedFlag,
                                                 this.taskCounter,
                                                 this.stopwords));
        return taskList;
    }
}
