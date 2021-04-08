package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.entities.Document;
import it.unibo.pcd.assignment1.concurrent.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} for transforming {@link Path}s into {@link Document}s.
 */
class PathFilterTask extends AbstractSingletonFilterTask<Path, Document> {
    /**
     * Default constructor.
     * @param pathPipe the {@link Pipe} from which dequeueing the {@link Path}s to be transformed
     * @param documentPipe the {@link Pipe} in which enqueueing the transformed {@link Document}s
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     */
    protected PathFilterTask(final Pipe<Path> pathPipe,
                             final Pipe<Document> documentPipe,
                             final AgentSuspendedFlag suspendedFlag,
                             final TaskCounter taskCounter) {
        super(pathPipe, documentPipe, suspendedFlag, taskCounter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PathFilterTask";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Document transformSingleton(final Path path) throws IOException {
        return new DocumentImpl(PDDocument.load(path.toFile()));
    }
}
