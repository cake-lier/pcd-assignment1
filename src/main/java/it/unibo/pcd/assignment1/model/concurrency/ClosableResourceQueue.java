package it.unibo.pcd.assignment1.model.concurrency;

import java.util.concurrent.BlockingQueue;

public interface ClosableResourceQueue<X> extends ResourceQueue<X> {
    void close();
}
