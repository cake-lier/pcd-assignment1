package it.unibo.pcd.assignment1.model.concurrency.pipe;

import java.util.Optional;

public interface PipeConnector<R,P> {
    void writeInPipe(P product);
    Optional<R> readFromPipe();
}
