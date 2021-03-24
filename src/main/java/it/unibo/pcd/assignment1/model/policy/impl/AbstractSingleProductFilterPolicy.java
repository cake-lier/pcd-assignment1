package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSingleProductFilterPolicy<R,P> extends AbstractFilterPolicy<R,P> {
    public AbstractSingleProductFilterPolicy(final Pipe<R> sourcePipe, final Pipe<P> productPipe, final SharedAgentState agentState) {
        super(sourcePipe, productPipe,agentState);
    }

    @Override
    protected List<P> transform(R resource) {
        return Collections.singletonList(transformSingleValue(resource));
    }

    abstract protected P transformSingleValue(R resource);
}
