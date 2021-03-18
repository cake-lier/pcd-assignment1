package it.unibo.pcd.assignment1.concurrency;

import it.unibo.pcd.assignment1.model.concurrency.ResourceQueue;
import it.unibo.pcd.assignment1.model.concurrency.ResourceQueueImpl;

public class ResourceQueueImplTest extends AbstractResourceQueueTest{
    @Override
    protected ResourceQueue<Integer> createIntegerQueue() {
        return new ResourceQueueImpl<>();
    }
}
