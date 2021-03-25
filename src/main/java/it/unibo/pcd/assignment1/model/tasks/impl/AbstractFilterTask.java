package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;
import it.unibo.pcd.assignment1.model.tasks.Task;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractFilterTask<I, O> implements Task {
    private final PipeConnector<I, O> pipeConnector;
    private final AgentSuspendedFlag suspendedFlag;

    public AbstractFilterTask(final PipeConnector<I, O> pipeConnector, final AgentSuspendedFlag suspendedFlag) {
        this.pipeConnector = Objects.requireNonNull(pipeConnector);
        this.suspendedFlag = Objects.requireNonNull(suspendedFlag);
    }

    @Override
    public void run() throws Exception {
        while (true) {
            this.suspendedFlag.check();
            final Optional<I> input = this.pipeConnector.read();
            if (input.isPresent()) {
                this.transform(input.get())
                    .forEach(o -> {
                        this.suspendedFlag.check();
                        this.pipeConnector.write(o);
                    });
            } else {
                break;
            }
        }
    }

    abstract protected Iterable<O> transform(I input) throws Exception;
}
