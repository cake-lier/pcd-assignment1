package it.unibo.pcd.assignment1.model.concurrency;

import java.util.concurrent.BlockingQueue;

public interface CloseableResourceQueue<X> extends ResourceQueue<X> {
    void close();
}
