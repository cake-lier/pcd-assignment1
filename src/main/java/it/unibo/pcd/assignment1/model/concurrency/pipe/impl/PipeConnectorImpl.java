package it.unibo.pcd.assignment1.model.concurrency.pipe.impl;

import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;

import java.util.Optional;

public class PipeConnectorImpl<R,P> implements PipeConnector<R,P> {
    private final Pipe<R> sourcePipe;
    private final Pipe<P> productPipe;

    public PipeConnectorImpl(final Pipe<R> sourcePipe, final Pipe<P> productPipe){
        this.sourcePipe = sourcePipe;
        this.productPipe = productPipe;
    }

    @Override
    public void writeInPipe(final P product) {
        this.productPipe.enqueue(product);
    }

    @Override
    public Optional<R> readFromPipe() {
        return this.sourcePipe.dequeue();
    }
}
