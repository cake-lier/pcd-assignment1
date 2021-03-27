package it.unibo.pcd.assignment1.concurrency;
import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterQueueTest extends AbstractGeneratorTaskPipeTest {

    private FilterPipe<Integer> closableResourceQueue;
    @BeforeEach
    public void init(){
        this.closableResourceQueue = new FilterPipeImpl<>();
    }

    @Test
    public void testClosable(){
        this.closableResourceQueue.close();
    }

    @Test
    public void testEnqueueAfterCloseException(){
        this.closableResourceQueue.close();
        Assertions.assertThrows(IllegalStateException.class,()-> this.closableResourceQueue.enqueue(0));
    }
    @Test
    public void testDequeueAfterCloseIsLegal(){
        this.fillQueue();
        closableResourceQueue.close();
        supportData.forEach(e->{
            final Optional<Integer> result = this.closableResourceQueue.dequeue();
            assertTrue(result.isPresent());
            result.ifPresent(value->assertEquals(e,value));
        });
    }
    @Test
    public void testDequeueAllOptionalEmptyIfClosed(){
        this.testDequeueAfterCloseIsLegal();
        supportData.forEach(e->assertEquals(Optional.empty(),closableResourceQueue.dequeue()));
    }

    @Override
    protected GeneratorPipe<Integer> createIntegerQueue() {
        return new FilterPipeImpl<>();
    }

    private void fillQueue(){
        this.fillQueue(supportData,closableResourceQueue);
    }
}
