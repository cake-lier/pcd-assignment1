package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractFilterTask<I, O> extends AbstractTask {
    private final PipeConnector<I, O> pipeConnector;

    public AbstractFilterTask(final PipeConnector<I, O> pipeConnector, final AgentSuspendedFlag suspendedFlag, final AgentTicketManager ticketManager) {
        super(suspendedFlag,ticketManager);
        this.pipeConnector = Objects.requireNonNull(pipeConnector);
    }

    @Override
    protected final boolean doAction() throws Exception{
        final Optional<I> input = this.pipeConnector.read();
        if (input.isPresent()) {
            this.transform(input.get())
                    .forEach(o -> {
                        this.getSuspendedFlag().check();
                        this.pipeConnector.write(o);
                    });
            return true;
        }
        return false;
    }

    @Override
    protected void doEnd() {
        this.pipeConnector.getOutputPipe().close();
    }

    abstract protected Iterable<O> transform(I input) throws Exception;
}
