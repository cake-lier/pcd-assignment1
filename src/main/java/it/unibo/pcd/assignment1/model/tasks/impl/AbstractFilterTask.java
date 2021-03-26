package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.Pipe;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractFilterTask<I, O> extends AbstractTask {
    private final Pipe<I> inputPipe;
    private final Pipe<O> outputPipe;

    public AbstractFilterTask(final Pipe<I> inputPipe, final Pipe<O> outputPipe, final AgentSuspendedFlag suspendedFlag, final AgentTicketManager ticketManager) {
        super(suspendedFlag,ticketManager);
        this.inputPipe = Objects.requireNonNull(inputPipe);
        this.outputPipe = Objects.requireNonNull(outputPipe);
    }

    @Override
    protected final boolean doAction() throws Exception{
        final Optional<I> input = this.inputPipe.dequeue();
        if (input.isPresent()) {
            this.transform(input.get())
                    .forEach(o -> {
                        this.getSuspendedFlag().check();
                        this.outputPipe.enqueue(o);
                    });
            return true;
        }
        return false;
    }

    @Override
    protected void doEnd() {
        this.outputPipe.close();
    }

    abstract protected Iterable<O> transform(I input) throws Exception;
}
