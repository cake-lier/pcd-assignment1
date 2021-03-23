package it.unibo.pcd.assignment1.model.concurrency;

public interface FilterPipe<E> extends GeneratorPipe<E> {
    void enqueue(E value);

    void close();
}
