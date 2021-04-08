package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.controller.tasks.impl.AbstractTask;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Objects;
import java.util.stream.IntStream;

class PathGeneratorTask extends AbstractTask {
    private static final String FILE_PATH = "file";

    private final Pipe<String> paths;
    private final int numberOfPaths;

    protected PathGeneratorTask(final int numberOfPaths, final Pipe<String> paths, final TaskCounter taskCounter) {
        super(taskCounter);
        this.paths = Objects.requireNonNull(paths);
        this.numberOfPaths = numberOfPaths;
    }

    @Override
    protected boolean doRun(){
        IntStream.rangeClosed(1, this.numberOfPaths).forEach(n -> this.paths.enqueue(FILE_PATH + n));
        return false;
    }

    @Override
    protected void doEnd() {
        this.paths.close();
    }
}
