package it.unibo.pcd.assignment1.jpf.model.pipes;

import java.util.Optional;

public interface Pipe<E> {
    Optional<E> dequeue();

    void enqueue(E value);

    void close();
}
