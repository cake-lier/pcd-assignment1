package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.pipes.Pipe;

import java.util.Collections;

abstract class AbstractSingletonFilterTask<I, O> extends AbstractFilterTask<I, O> {
    protected AbstractSingletonFilterTask(final Pipe<I> inputPipe,
                                          final Pipe<O> outputPipe,
                                          final AgentSuspendedFlag agentState,
                                          final TaskCounter taskCounter) {
        super(agentState, taskCounter, inputPipe, outputPipe);
    }

    @Override
    protected Iterable<O> transform(final I input) throws Exception {
        return Collections.singletonList(this.transformSingleton(input));
    }

    protected abstract O transformSingleton(final I input) throws Exception;
}
