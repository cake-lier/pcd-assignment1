package it.unibo.pcd.assignment1.concurrent.model.pipes;

import java.util.Optional;

/**
 * A pipeline that can hold resources that are produced by a task and consumed by another, sharing them between those two.
 * @param <E> the type of resource contained into this pipeline
 */
public interface Pipe<E> {
    /**
     * It dequeues an element from this pipeline. This operation is blocking if no elements are present inside the pipeline,
     * unless the pipeline is closed. If it is closed, then an {@link Optional#empty()} returned.
     * @return the next element in the pipeline, if present, an {@link Optional#empty()} otherwise
     */
    Optional<E> dequeue();

    /**
     * It enqueues an element into this pipeline. This operation is only possible if the pipeline is not closed, after that an
     * exception is thrown.
     * @param value the value to add to the pipeline
     */
    void enqueue(E value);

    /**
     * It closes this pipeline, waking up all tasks waiting for dequeueing an element.
     */
    void close();
}
