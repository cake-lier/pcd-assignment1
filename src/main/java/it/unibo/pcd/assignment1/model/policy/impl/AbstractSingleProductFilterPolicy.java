package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSingleProductFilterPolicy<R,P> extends AbstractFilterPolicy<R,P> {
    public AbstractSingleProductFilterPolicy(final PipeConnector<R,P> connector, final SharedAgentState agentState) {
        super(connector,agentState);
    }

    @Override
    protected List<P> transform(R resource) {
        return Collections.singletonList(transformSingleValue(resource));
    }

    abstract protected P transformSingleValue(R resource);
}
