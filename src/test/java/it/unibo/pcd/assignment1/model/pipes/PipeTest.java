package it.unibo.pcd.assignment1.model.pipes;

import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.pipes.impl.BoundedPipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PipeTest {
    private static final int MAX_ELEMENTS_NUMBER = 10;
    private static final int SUPPORT_DATA_LENGTH = 5;
    private static final int SLEEP_SECONDS = 10;
    private static final int SINGLE_INT_VALUE = 0;

    private List<Integer> supportData;
    private Pipe<Integer> pipe;

    @BeforeEach
    public void setUp() {
        this.supportData = new Random().ints(SUPPORT_DATA_LENGTH).boxed().collect(Collectors.toList());
        this.pipe = new BoundedPipe<>(MAX_ELEMENTS_NUMBER);
    }

    @Test
    public void testDequeueOnEmptyPipe() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            this.pipe.dequeue();
            latch.countDown();
        }).start();
        if (latch.await(SLEEP_SECONDS, TimeUnit.SECONDS)) {
            Assertions.fail();
        }
    }

    @Test
    public void testDequeueMultiThreaded() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(SUPPORT_DATA_LENGTH);
        this.supportData.forEach(this.pipe::enqueue);
        this.supportData.forEach(e -> new Thread(() -> {
            final Optional<Integer> result = this.pipe.dequeue();
            if (result.isPresent()) {
                latch.countDown();
            }
        }).start());
        if (!latch.await(SLEEP_SECONDS, TimeUnit.SECONDS)) {
            Assertions.fail();
        }
    }

    @Test
    public void testEnqueue() {
        this.pipe.enqueue(SINGLE_INT_VALUE);
        Assertions.assertEquals(Optional.of(SINGLE_INT_VALUE), this.pipe.dequeue());
    }

    @Test
    public void testDequeueSingleThreaded() {
        this.supportData.forEach(this.pipe::enqueue);
        this.supportData.forEach(e -> {
            final Optional<Integer> result = this.pipe.dequeue();
            Assertions.assertTrue(result.isPresent());
            result.ifPresent(i -> Assertions.assertEquals(e, i));
        });
    }

    @Test
    public void testClose() {
        this.pipe.close();
    }

    @Test
    public void testEnqueueAfterClosed() {
        this.pipe.close();
        Assertions.assertThrows(IllegalStateException.class, () -> this.pipe.enqueue(0));
    }

    @Test
    public void testDequeueAfterClosed() {
        this.supportData.forEach(this.pipe::enqueue);
        this.pipe.close();
        this.supportData.forEach(e -> {
            final Optional<Integer> result = this.pipe.dequeue();
            Assertions.assertTrue(result.isPresent());
            result.ifPresent(v -> Assertions.assertEquals(e, v));
        });
    }

    @Test
    public void testDequeueOnEmptyPipeAfterClosed() {
        this.pipe.close();
        this.supportData.forEach(e -> Assertions.assertEquals(Optional.empty(), this.pipe.dequeue()));
    }
}
