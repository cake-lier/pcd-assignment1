package it.unibo.pcd.assignment1.concurrent.model.pipes;

import it.unibo.pcd.assignment1.concurrent.model.entities.Update;

import java.util.Optional;

/**
 * An extension of a {@link Pipe} for {@link Update}s which can also be drained of all resources contained in it.
 */
public interface WordCounter extends Pipe<Update> {
    /**
     * It drains the pipeline of all resources contained in it, while returning the most recently added one, if present.
     * @return the most recently added resource, if present, an {@link Optional#empty()} otherwise
     */
    Optional<Update> drain();
}
