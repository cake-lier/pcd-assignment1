package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.controller.tasks.impl.AbstractTask;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Objects;
import java.util.Optional;

abstract class AbstractFilterTask<I, O> extends AbstractTask {
    private final Pipe<I> inputPipe;
    private final Pipe<O> outputPipe;

    protected AbstractFilterTask(final TaskCounter taskCounter,
                                 final Pipe<I> inputPipe,
                                 final Pipe<O> outputPipe) {
        super(taskCounter);
        this.inputPipe = Objects.requireNonNull(inputPipe);
        this.outputPipe = Objects.requireNonNull(outputPipe);
    }

    @Override
    protected boolean doRun(){
        final Optional<I> input = this.inputPipe.dequeue();
        if (input.isPresent()) {
            this.transform(input.get())
                .forEach(o -> this.outputPipe.enqueue(o));
        }
        return input.isPresent();
    }

    @Override
    protected void doEnd() {
        this.outputPipe.close();
    }

    protected abstract Iterable<O> transform(final I input);
}
