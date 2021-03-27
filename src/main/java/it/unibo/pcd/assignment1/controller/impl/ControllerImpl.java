package it.unibo.pcd.assignment1.controller.impl;

import it.unibo.pcd.assignment1.controller.Controller;
import it.unibo.pcd.assignment1.controller.agents.impl.AgentImpl;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.impl.BoundedPipe;
import it.unibo.pcd.assignment1.model.pipes.impl.WordCounter;
import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.model.shared.impl.AgentSuspendedFlagImpl;
import it.unibo.pcd.assignment1.model.shared.impl.StopwordsSetImpl;
import it.unibo.pcd.assignment1.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.tasks.impl.TaskCounterImpl;
import it.unibo.pcd.assignment1.model.tasks.impl.TaskListFactory;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.view.View;
import it.unibo.pcd.assignment1.model.entities.DocumentImpl;
import it.unibo.pcd.assignment1.model.entities.Page;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ControllerImpl implements Controller {
    private static final int PIPE_MAX_NUMBER = 100;
    private static final int TOTAL_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    //TODO: use formula Ncpu * Ucpu * (1 + w/c)

    private final View view;
    private final AgentSuspendedFlag suspendedFlag;
    private final TaskCounter taskCounter;
    private final Consumer<Exception> exceptionHandler;

    public ControllerImpl(final View view) {
        this.view = Objects.requireNonNull(view);
        this.suspendedFlag = new AgentSuspendedFlagImpl();
        this.taskCounter = new TaskCounterImpl();
        this.exceptionHandler = e -> this.view.displayError(e.getMessage());
    }

    @Override
    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        final StopwordsSet stopwords = new StopwordsSetImpl();
        final Pipe<Path> paths = new BoundedPipe<>(PIPE_MAX_NUMBER);
        final Pipe<DocumentImpl> documents = new BoundedPipe<>(PIPE_MAX_NUMBER);
        final Pipe<Page> pages = new BoundedPipe<>(PIPE_MAX_NUMBER);
        final WordCounter updates = new WordCounter(PIPE_MAX_NUMBER, wordsNumber);
        new AgentImpl(
            TaskListFactory.createForGeneratorAgent(filesDirectory,
                                                    stopwordsFile,
                                                    this.suspendedFlag,
                                                    this.taskCounter,
                                                    paths,
                                                    documents,
                                                    pages,
                                                    updates,
                                                    stopwords),
            this.exceptionHandler
        )
        .start();
        Stream.generate(() -> Arrays.asList(FilterTaskTypes.values()))
              .flatMap(List::stream)
              .limit(Math.max(TOTAL_THREADS - 2, FilterTaskTypes.values().length))
              .forEach(t -> new AgentImpl(
                  TaskListFactory.createForFilterAgent(t,
                                                       paths,
                                                       documents,
                                                       pages,
                                                       updates,
                                                       this.suspendedFlag,
                                                       this.taskCounter,
                                                       stopwords),
                  this.exceptionHandler
              )
              .start());
        new AgentImpl(
            TaskListFactory.createForSinkAgent(updates,
                                               u -> this.view.displayProgress(u.getFrequencies(), u.getProcessedWords()),
                                               this.view::displayCompletion,
                                               this.suspendedFlag,
                                               this.taskCounter),
            this.exceptionHandler
        )
        .start();
    }

    @Override
    public void suspend() {
        this.suspendedFlag.setSuspended();
    }

    @Override
    public void resume() {
        this.suspendedFlag.setRunning();
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
