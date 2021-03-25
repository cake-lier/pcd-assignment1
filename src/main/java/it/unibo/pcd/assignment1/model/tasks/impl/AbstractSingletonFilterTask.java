package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;

import java.util.Collections;

public abstract class AbstractSingletonFilterTask<I, O> extends AbstractFilterTask<I, O> {
    public AbstractSingletonFilterTask(final PipeConnector<I, O> connector, final AgentSuspendedFlag agentState, final AgentTicketManager ticketManager) {
        super(connector, agentState,ticketManager);
    }

    @Override
    protected Iterable<O> transform(final I input) throws Exception {
        return Collections.singletonList(this.transformSingleton(input));
    }

    abstract protected O transformSingleton(final I input) throws Exception;
}
