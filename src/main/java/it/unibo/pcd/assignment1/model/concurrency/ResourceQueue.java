package it.unibo.pcd.assignment1.model.concurrency;

import java.util.Optional;

public interface ResourceQueue<X> {

    void add(X data);
    Optional<X> pop();
}
