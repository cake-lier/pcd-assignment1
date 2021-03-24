package it.unibo.pcd.assignment1.model.concurrency;

import java.util.Optional;

public interface Pipe<E> {
    Optional<E> dequeue();
    void enqueue(E value);
    void close();
}
