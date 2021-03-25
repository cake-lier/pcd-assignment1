package it.unibo.pcd.assignment1.model.pipes.impl;

import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;

import java.util.Optional;

public class PipeConnectorImpl<R,P> implements PipeConnector<R,P> {
    private final Pipe<R> sourcePipe;
    private final Pipe<P> productPipe;

    public PipeConnectorImpl(final Pipe<R> sourcePipe, final Pipe<P> productPipe){
        this.sourcePipe = sourcePipe;
        this.productPipe = productPipe;
    }

    @Override
    public void write(final P product) {
        this.productPipe.enqueue(product);
    }

    @Override
    public Optional<R> readFromPipe() {
        return this.sourcePipe.dequeue();
    }
}
