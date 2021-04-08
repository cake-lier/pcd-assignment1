package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractSingletonFilterTask<I, O> extends AbstractFilterTask<I, O> {
    protected AbstractSingletonFilterTask(final Pipe<I> inputPipe,
                                          final Pipe<O> outputPipe,
                                          final TaskCounter taskCounter) {
        super(taskCounter, inputPipe, outputPipe);
    }

    @Override
    protected Iterable<O> transform(final I input){
        List<O> list = new ArrayList<>();
        list.add(this.transformSingleton(input));
        return list;
    }

    protected abstract O transformSingleton(I input);
}
