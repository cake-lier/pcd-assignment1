package it.unibo.pcd.assignment1.concurrency;

import it.unibo.pcd.assignment1.model.concurrency.ClosableResourceQueue;
import it.unibo.pcd.assignment1.model.concurrency.ClosableResourceQueueImpl;
import it.unibo.pcd.assignment1.model.concurrency.ResourceQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosableResourceQueueTest extends AbstractResourceQueueTest{

    private ClosableResourceQueue<Integer> closableResourceQueue;
    @BeforeEach
    public void init(){
        this.closableResourceQueue = new ClosableResourceQueueImpl<>();
    }

    @Test
    public void testClosable(){
        this.closableResourceQueue.close();
    }

    @Test
    public void testAddAfterCloseException(){
        this.closableResourceQueue.close();
        Assertions.assertThrows(IllegalStateException.class,()-> this.closableResourceQueue.add(0));
    }
    @Test
    public void testPopAfterCloseIsLegal(){
        this.fillQueue();
        closableResourceQueue.close();
        supportData.forEach(e->assertEquals(e,closableResourceQueue.pop().get()));
    }
    @Test
    public void testPopAllOptionalEmptyIfClosed(){
        this.testPopAfterCloseIsLegal();
        supportData.forEach(e->assertEquals(Optional.empty(),closableResourceQueue.pop()));
    }

    @Override
    protected ResourceQueue<Integer> createIntegerQueue() {
        return new ClosableResourceQueueImpl<>();
    }

    private void fillQueue(){
        this.fillQueue(supportData,closableResourceQueue);
    }
}
