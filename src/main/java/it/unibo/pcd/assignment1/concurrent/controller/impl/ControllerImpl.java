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

/**
 * An implementation of the {@link Controller} interface.
 */
public class ControllerImpl implements Controller {
    private static final int PIPES_SIZE = 100;
    private static final int TOTAL_THREADS = Math.round(Runtime.getRuntime().availableProcessors() * 1.0f * (1 + 1.093f));

    private final View view;
    private final AgentSuspendedFlag suspendedFlag;
    private final Consumer<Exception> exceptionHandler;

    /**
     * Default constructor.
     * @param view the View component to be notified by this Controller instance
     */
    public ControllerImpl(final View view) {
        this.view = Objects.requireNonNull(view);
        this.suspendedFlag = new AgentSuspendedFlagImpl();
        this.exceptionHandler = e -> this.view.displayError(e.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void launch(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) {
        this.suspendedFlag.setRunning();
        final TaskCounter taskCounter = new TaskCounterImpl();
        final WordCounter updates = new WordCounterImpl(PIPES_SIZE, wordsNumber);
        final TaskListFactory taskListFactory = new TaskListFactory(PIPES_SIZE, updates, this.suspendedFlag, taskCounter);
        Stream.concat(Stream.of(new AgentImpl(taskListFactory.createForGeneratorAgent(filesDirectory, stopwordsFile),
                                              this.exceptionHandler),
                                new AgentImpl(Collections.singletonList(new UpdateSinkTask(updates,
                                                                                           this.view,
                                                                                           this.suspendedFlag,
                                                                                           taskCounter)),
                                              this.exceptionHandler)),
                      Stream.generate(() -> Arrays.asList(FilterTaskTypes.values()))
                            .flatMap(List::stream)
                            .limit(TOTAL_THREADS - 2)
                            .map(t -> new AgentImpl(taskListFactory.createForFilterAgent(t), this.exceptionHandler)))
              .forEach(Agent::go);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void suspend() {
        this.suspendedFlag.setSuspended();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        this.suspendedFlag.setRunning();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exit() {
        System.exit(0);
    }
}
