package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.controller.tasks.impl.AbstractTask;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} for generating the inputs of the computation, the
 * {@link Path}s.
 */
class PathGeneratorTask extends AbstractTask {
    private final Path filesDirectory;
    private final Path stopwordsFile;
    private final StopwordsSet stopwords;
    private final Pipe<Path> paths;

    /**
     * Default constructor.
     * @param filesDirectory the path of the directory containing the PDF files to process
     * @param stopwordsFile the path of the file containing the stopwords
     * @param paths the {@link Pipe} in which enqueueing the generated {@link Path}s
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     * @param stopwords the {@link StopwordsSet} to populate
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PathGeneratorTask";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doRun() throws Exception {
        this.stopwords.set(Files.readAllLines(this.stopwordsFile));
        Files.list(this.filesDirectory)
             .filter(p -> p.toString().matches(".*pdf$"))
             .forEach(this.paths::enqueue);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEnd() {
        this.paths.close();
    }
}
