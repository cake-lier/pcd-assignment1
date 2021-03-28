package it.unibo.pcd.assignment1.concurrent.model.pipes;

import it.unibo.pcd.assignment1.concurrent.model.entities.Update;

import java.util.Optional;

public interface WordCounter extends Pipe<Update> {
    Optional<Update> drain();
}
