package it.unibo.pcd.assignment1.model.worker;

import it.unibo.pcd.assignment1.model.WordCounter;
import it.unibo.pcd.assignment1.model.concurrency.ResourceQueueImpl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorkerPoolImpl implements WorkerPool {
    private static final int MAX_NUM_WORKERS = Runtime.getRuntime().availableProcessors() + 1;  //TODO: NCpu * UCpu * (1 + W/C)

    private final SharedWorkerState sharedState;
    private final Collection<Worker> workers;

    public WorkerPoolImpl(final Set<Path> filesToProcess,
                          final Set<String> stopwords,
                          final int wordsNumber,
                          final WordCounter sharedCounter) {
        this.sharedState = new SharedWorkerStateImpl();
        this.workers = IntStream.range(0, MAX_NUM_WORKERS)
                                .mapToObj(i -> new WorkerImpl(new ResourceQueueImpl<>(filesToProcess),
                                                              null,
                                                              stopwords,
                                                              this.sharedState,
                                                              sharedCounter))
                                .collect(Collectors.toList());
    }

    @Override
    public void start() {
        this.workers.forEach(Worker::start);
    }

    @Override
    public void suspend() {
        this.sharedState.setSuspendedState();
    }

    @Override
    public void resume() {
        this.sharedState.setRunningState();
    }
}
