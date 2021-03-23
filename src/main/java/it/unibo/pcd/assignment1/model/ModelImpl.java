package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.worker.WorkerPool;
import it.unibo.pcd.assignment1.model.worker.WorkerPoolImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelImpl implements Model {
    private Optional<WorkerPool> workerPool;
    private Optional<WordCounterImpl> sharedCounter;

    public ModelImpl() {
        this.sharedCounter = Optional.empty();
        this.workerPool = Optional.empty();
    }

    @Override
    public void startCalculation(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) throws IOException {
        this.sharedCounter = Optional.of(new WordCounterImpl(wordsNumber));
        this.workerPool = Optional.of(new WorkerPoolImpl(Files.list(filesDirectory)
                                                              .filter(p -> p.toString().matches(".*pdf$"))
                                                              .collect(Collectors.toSet()),
                                                         new HashSet<>(Files.readAllLines(stopwordsFile)),
                                                         this.sharedCounter.get()));
        this.workerPool.get().start();
    }

    @Override
    public void suspendCalculation() {
        this.workerPool.ifPresent(WorkerPool::suspend);
    }

    @Override
    public void resumeCalculation() {
        this.workerPool.ifPresent(WorkerPool::resume);
    }

    @Override
    public Optional<Update> getLatestUpdate() {
        return this.sharedCounter.flatMap(WordCounterImpl::dequeue);
    }
}
