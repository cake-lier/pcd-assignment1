package it.unibo.pcd.assignment1.model.worker;

import it.unibo.pcd.assignment1.model.Page;
import it.unibo.pcd.assignment1.model.WordCounterImpl;
import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipe;
import it.unibo.pcd.assignment1.model.concurrency.GeneratorPipeImpl;

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
                          final WordCounterImpl sharedCounter) {
        this.sharedState = new SharedWorkerStateImpl();
        final GeneratorPipe<Path> documentsPipe = new GeneratorPipeImpl<>(filesToProcess);
        final FilterPipe<Page> pagesPipe = new FilterPipeImpl<>();
        this.workers = IntStream.range(0, MAX_NUM_WORKERS)
                                .mapToObj(i -> new WorkerImpl(documentsPipe,
                                                              pagesPipe,
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
