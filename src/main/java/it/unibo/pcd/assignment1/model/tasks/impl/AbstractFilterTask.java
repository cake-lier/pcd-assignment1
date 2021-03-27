package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.pipes.Pipe;

import java.util.Objects;
import java.util.Optional;

abstract class AbstractFilterTask<I, O> extends AbstractTask {
    private final Pipe<I> inputPipe;
    private final Pipe<O> outputPipe;

    protected AbstractFilterTask(final AgentSuspendedFlag suspendedFlag,
                                 final TaskCounter taskCounter,
                                 final Pipe<I> inputPipe,
                                 final Pipe<O> outputPipe) {
        super(suspendedFlag, taskCounter);
        this.inputPipe = Objects.requireNonNull(inputPipe);
        this.outputPipe = Objects.requireNonNull(outputPipe);
    }

    @Override
    protected boolean doRun() throws Exception {
        final Optional<I> input = this.inputPipe.dequeue();
        if (input.isPresent()) {
            this.transform(input.get())
                .forEach(o -> {
                    this.getSuspendedFlag().check();
                    this.outputPipe.enqueue(o);
                });
        }
        return input.isPresent();
    }

    @Override
    protected void doEnd() {
        this.outputPipe.close();
    }

    protected abstract Iterable<O> transform(final I input) throws Exception;
}
