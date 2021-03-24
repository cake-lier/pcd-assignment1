package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;
import it.unibo.pcd.assignment1.model.policy.Policy;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;

public abstract class AbstractPipePolicy<R,P> implements Policy {
    private final PipeConnector<R,P> pipeConnector;

    public AbstractPipePolicy(PipeConnector<R,P> pipeConnector){
        this.pipeConnector = pipeConnector;
    }

    public PipeConnector<R,P> getConnector(){
        return this.pipeConnector;
    }
}

