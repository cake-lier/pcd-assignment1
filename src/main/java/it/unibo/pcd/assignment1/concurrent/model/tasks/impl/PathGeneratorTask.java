package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.controller.tasks.impl.AbstractTask;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class PathGeneratorTask extends AbstractTask {
    private final Path filesDirectory;
    private final Path stopwordsFile;
    private final StopwordsSet stopwords;
    private final Pipe<Path> paths;

    protected PathGeneratorTask(final Path filesDirectory,
                                final Path stopwordsFile,
                                final Pipe<Path> paths,
                                final AgentSuspendedFlag suspendedFlag,
                                final TaskCounter taskCounter,
                                final StopwordsSet stopwords) {
        super(suspendedFlag, taskCounter);
        this.filesDirectory = Objects.requireNonNull(filesDirectory);
        this.stopwordsFile = Objects.requireNonNull(stopwordsFile);
        this.stopwords = Objects.requireNonNull(stopwords);
        this.paths = Objects.requireNonNull(paths);
    }

    @Override
    public String toString() {
        return "PathGeneratorTask";
    }

    @Override
    protected boolean doRun() throws Exception {
        this.stopwords.set(Files.readAllLines(this.stopwordsFile));
        Files.list(this.filesDirectory)
             .filter(p -> p.toString().matches(".*pdf$"))
             .forEach(this.paths::enqueue);
        return false;
    }

    @Override
    protected void doEnd() {
        this.paths.close();
    }
}
