package it.unibo.pcd.assignment1.model.pipes;

import java.util.Optional;

public interface PipeConnector<I, O> {
    void write(O output);

    Optional<I> read();

    Pipe<I> getInputPipe();

    Pipe<O> getOutputPipe();
}
