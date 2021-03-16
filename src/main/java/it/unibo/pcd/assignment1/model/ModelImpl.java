package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateFactory;
import it.unibo.pcd.assignment1.model.worker.Worker;
import it.unibo.pcd.assignment1.model.worker.WorkerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelImpl implements Model {
    private static final int MAX_NUM_WORKERS = Runtime.getRuntime().availableProcessors() + 1;  //TODO: NCpu * UCpu * (1 + W/C)

    private final List<Worker> workers;
    private Optional<WordCounter> counter;

    public ModelImpl() {
        this.workers = new ArrayList<>();
        this.counter = Optional.empty();
    }

    @Override
    public void startCalculation(final Path filesDirectory, final Path stopwordsFile, final int wordsNumber) throws IOException {
        this.workers.clear();
        this.counter = Optional.of(new WordCounterImpl(wordsNumber));
        final List<Path> files = Files.list(filesDirectory).collect(Collectors.toList());
        final List<String> stopwords = Files.readAllLines(stopwordsFile);
        for (final List<Path> partition : files.stream()
                                               .collect(Collectors.groupingBy(p -> files.indexOf(p) % MAX_NUM_WORKERS))
                                               .values()) {
            final Worker worker = WorkerFactory.create(partition, stopwords, this.counter.get());
            this.workers.add(worker);
            worker.start();
        }
    }

    @Override
    public void suspendCalculation() {
        this.counter.ifPresent(WordCounter::suspend);
    }

    @Override
    public void resumeCalculation() {
        this.counter.ifPresent(WordCounter::resume);
    }

    @Override
    public boolean isCalculationCompleted() {
        return this.workers.parallelStream().allMatch(Worker::hasCompleted);
    }

    @Override
    public void awaitResult() {
        this.workers.forEach(Worker::await);
    }

    @Override
    public Update getUpdate() {
        return this.counter.map(WordCounter::getCurrentState).orElse(UpdateFactory.empty());
    }
}
