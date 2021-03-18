package it.unibo.pcd.assignment1.concurrency;

import it.unibo.pcd.assignment1.model.concurrency.ResourceQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractResourceQueueTest {
    private static final int SUPPORT_DATA_LENGTH = 30;
    private static final int SLEEP_MILLISECONDS = 1000;
    private static final int SINGLE_INT_VALUE = 0;
    protected static List<Integer> supportData;
    protected static ResourceQueue<Integer> resourceQueue;

    @BeforeEach
    public void  initialize(){
        supportData= new Random().ints(SUPPORT_DATA_LENGTH).boxed().collect(Collectors.toList());
        resourceQueue = createIntegerQueue();
    }

    @Test
    public void testWaitOnPopEmptyList() throws InterruptedException {
        AtomicBoolean fail = new AtomicBoolean(false);
        this.createThread(()->{
            createIntegerQueue().pop();
            fail.set(true);
        });
        Thread.sleep(SLEEP_MILLISECONDS);
        assertFalse(fail.get());
    }
    @Test
    public void testInsertOneIsPresent(){
        resourceQueue.add(SINGLE_INT_VALUE);
        assertEquals(Optional.of(SINGLE_INT_VALUE),resourceQueue.pop());
    }

    @Test
    public void testInsertMany(){
        this.fillQueue();
    }

    @Test
    public void testPopValues(){
        this.fillQueue();
        supportData.forEach(e->assertEquals(e,resourceQueue.pop().get()));
    }

    protected void fillQueue(Collection<Integer> collection,ResourceQueue<Integer> resourceQueue){
        collection.forEach(resourceQueue::add);
    }

    private void fillQueue(){
        this.fillQueue(supportData,resourceQueue);
    }

    protected void createThread(Runnable runnable){
        new Thread(runnable).start();
    }

    abstract protected  ResourceQueue<Integer> createIntegerQueue();

}
