package it.unibo.pcd.assignment1.concurrency;

import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractGeneratorPipeTest {
    private static final int SUPPORT_DATA_LENGTH = 30;
    private static final int SLEEP_MILLISECONDS = 1000;
    private static final int SINGLE_INT_VALUE = 0;
    protected List<Integer> supportData;
    protected GeneratorPipe<Integer> generatorPipe;

    @BeforeEach
    public void  initialize(){
        this.supportData= new Random().ints(SUPPORT_DATA_LENGTH).boxed().collect(Collectors.toList());
        this.generatorPipe = createIntegerQueue();
    }

    @Test
    public void testWaitOnQueueEmptyList() throws InterruptedException {
        final AtomicBoolean fail = new AtomicBoolean(false);
        this.createThread(()->{
            createIntegerQueue().dequeue();
            fail.set(true);
        });
        Thread.sleep(SLEEP_MILLISECONDS);
        assertFalse(fail.get());
    }

    @Test
    public void testMultipleDequeueThread(){
        final AtomicBoolean flag = new AtomicBoolean(true);
        final AtomicInteger remainingThreads = new AtomicInteger(supportData.size());
        this.fillQueue();

        supportData.forEach(e-> {
            try {
                createThread(()->{
                    final Optional<Integer> result = this.generatorPipe.dequeue();
                    if(!result.isPresent()){
                       flag.set(false);
                    }
                    remainingThreads.decrementAndGet();
                }).join(SLEEP_MILLISECONDS);
            } catch (InterruptedException interruptedException) {
                fail(interruptedException);
            }
        });
        assertEquals(0,remainingThreads.get());
        assertTrue(flag.get());
    }

    @Test
    public void testEnqueueOneIsPresent(){
        generatorPipe.enqueue(SINGLE_INT_VALUE);
        assertEquals(Optional.of(SINGLE_INT_VALUE), generatorPipe.dequeue());
    }

    @Test
    public void testEnqueueMany(){
        this.fillQueue();
    }

    @Test
    public void testDequeueValues(){
        this.fillQueue();
        supportData.forEach(e-> {
            final Optional<Integer> result = generatorPipe.dequeue();
            assertTrue(result.isPresent());
            result.ifPresent(integer -> assertEquals(e, integer));
        });
    }

    protected void fillQueue(Collection<Integer> collection, GeneratorPipe<Integer> generatorPipe){
        collection.forEach(generatorPipe::enqueue);
    }

    private void fillQueue(){
        this.fillQueue(supportData, generatorPipe);
    }

    protected Thread createThread(Runnable runnable){
        final Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    abstract protected GeneratorPipe<Integer> createIntegerQueue();

}
