package it.unibo.pcd.assignment1.concurrent.controller.impl;

import it.unibo.pcd.assignment1.concurrent.controller.Controller;
import it.unibo.pcd.assignment1.concurrent.controller.agents.Agent;
import it.unibo.pcd.assignment1.concurrent.controller.agents.impl.AgentImpl;
import it.unibo.pcd.assignment1.concurrent.controller.tasks.impl.UpdateSinkTask;
import it.unibo.pcd.assignment1.concurrent.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.concurrent.model.pipes.impl.WordCounterImpl;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.shared.impl.AgentSuspendedFlagImpl;
import it.unibo.pcd.assignment1.concurrent.model.tasks.FilterTaskTypes;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.tasks.impl.TaskCounterImpl;
import it.unibo.pcd.assignment1.concurrent.model.tasks.impl.TaskListFactory;
import it.unibo.pcd.assignment1.concurrent.view.View;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ControllerImpl implements Controller {
    private static final int PIPES_SIZE = 100;
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
        final WordCounter updates = new WordCounterImpl(PIPES_SIZE, wordsNumber);
        final TaskListFactory taskListFactory = new TaskListFactory(PIPES_SIZE, updates, this.suspendedFlag, this.taskCounter);
        Stream.concat(Stream.of(new AgentImpl(taskListFactory.createForGeneratorAgent(filesDirectory, stopwordsFile),
                                              this.exceptionHandler),
                                new AgentImpl(Collections.singletonList(new UpdateSinkTask(updates,
                                                                                           this.view,
                                                                                           this.suspendedFlag,
                                                                                           this.taskCounter)),
                                              this.exceptionHandler)),
                      Stream.generate(() -> Arrays.asList(FilterTaskTypes.values()))
                            .flatMap(List::stream)
                            .limit(Math.max(TOTAL_THREADS - 2, FilterTaskTypes.values().length))
                            .map(t -> new AgentImpl(taskListFactory.createForFilterAgent(t), this.exceptionHandler)))
              .forEach(Agent::go);
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
