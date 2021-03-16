package it.unibo.pcd.assignment1.model.worker;

import it.unibo.pcd.assignment1.model.WordCounter;

import java.nio.file.Path;
import java.util.List;

public class WorkerFactory {
    private WorkerFactory() {}

    public static Worker create(final List<Path> filesToProcess, final List<String> stopwords, final WordCounter counter) {
        return new WorkerImpl(filesToProcess, stopwords, counter);
    }
}
