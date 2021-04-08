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

/**
 * A factory for the lists of {@link Task}s which can be executed by an
 * {@link it.unibo.pcd.assignment1.concurrent.controller.agents.Agent}.
 */
public class TaskListFactory {
    private final Pipe<Path> paths;
    private final Pipe<Document> documents;
    private final Pipe<Page> pages;
    private final Pipe<Update> updates;
    private final AgentSuspendedFlag suspendedFlag;
    private final TaskCounter taskCounter;
    private final StopwordsSet stopwords;

    /**
     * Default constructor.
     * @param pipesSize the size of the {@link Pipe}s to be created
     * @param updates the special {@link Pipe} for {@link Update}s
     * @param suspendedFlag the flag for checking whether the execution of a {@link Task} should be suspended or not
     * @param taskCounter the counter to which register for notifying a {@link Task} has started running or has ended
     */
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

    /**
     * It creates a new list of {@link Task}s for a generic "filter"
     * {@link it.unibo.pcd.assignment1.concurrent.controller.agents.Agent}.
     * @param firstTaskType the type of the first {@link Task} to be executed by the agent
     * @return a new list of {@link Task}s for a generic "filter"
     * {@link it.unibo.pcd.assignment1.concurrent.controller.agents.Agent}
     */
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

    /**
     * It creates a new list of {@link Task}s for a "generator"
     * {@link it.unibo.pcd.assignment1.concurrent.controller.agents.Agent}.
     * @param filesDirectory the path of the directory containing the PDF files to process
     * @param stopwordsFile the path of the file containing the stopwords
     * @return a new list of {@link Task}s for a "generator" {@link it.unibo.pcd.assignment1.concurrent.controller.agents.Agent}
     */
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
