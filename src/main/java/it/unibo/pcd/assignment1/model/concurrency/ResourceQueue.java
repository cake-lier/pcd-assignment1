package it.unibo.pcd.assignment1.model.concurrency;

import java.util.Optional;

public interface ResourceQueue<E> {

    void enqueue(E value);

    Optional<E> dequeue();
}
