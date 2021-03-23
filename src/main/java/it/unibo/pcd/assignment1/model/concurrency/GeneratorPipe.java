package it.unibo.pcd.assignment1.model.concurrency;

import java.util.Optional;

public interface GeneratorPipe<E> {
    Optional<E> dequeue();
}
