package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.policy.Policy;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;

public abstract class AbstractPipePolicy<R,P> implements Policy {
    private final Pipe<R> sourcePipe;
    private final Pipe<P> productPipe;

    public AbstractPipePolicy(final Pipe<R> sourcePipe,final Pipe<P> productPipe){
        this.sourcePipe = sourcePipe;
        this.productPipe = productPipe;
    }

    public Pipe<P> getProductPipe() {
        return productPipe;
    }

    public Pipe<R> getSourcePipe() {
        return sourcePipe;
    }
}
