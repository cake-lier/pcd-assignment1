package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;

import java.util.List;
import java.util.Optional;

public abstract class AbstractFilterPolicy<R,P> extends AbstractPipePolicy<R,P> {
    private final SharedAgentState agentState;

    public AbstractFilterPolicy(final PipeConnector<R,P> pipeConnector, final SharedAgentState agentState) {
        super(pipeConnector);
        this.agentState = agentState;
    }

    @Override
    public void start(){
        while(true){
            this.agentState.checkForSuspension();
            final Optional<R> resource = this.getConnector().readFromPipe();
            if(resource.isPresent()){
                this.transform(resource.get()).forEach(e->{
                    this.getConnector().writeInPipe(e);
                });
            }else{
                break;
            }
        }
    }

    abstract protected List<P> transform(R resource);
}
