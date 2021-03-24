package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;

import java.util.List;
import java.util.Optional;

public abstract class AbstractFilterPolicy<R,P> extends AbstractPipePolicy<R,P> {
    private final SharedAgentState agentState;

    public AbstractFilterPolicy(final Pipe<R> sourcePipe,final Pipe<P> productPipe,final SharedAgentState agentState) {
        super(sourcePipe, productPipe);
        this.agentState = agentState;
    }

    @Override
    public void start(){
        while(true){
            this.agentState.checkForSuspension();
            final Optional<R> resource = this.getSourcePipe().dequeue();
            if(resource.isPresent()){
                this.transform(resource.get()).forEach(e->{
                    this.getProductPipe().enqueue(e);
                });
            }else{
                break;
            }
        }
    }

    abstract protected List<P> transform(R resource);
}
