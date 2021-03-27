package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.entities.DocumentImpl;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;

class PathFilterTask extends AbstractSingletonFilterTask<Path, DocumentImpl> {
    protected PathFilterTask(final Pipe<Path> pathPipe,
                             final Pipe<DocumentImpl> documentPipe,
                             final AgentSuspendedFlag agentState,
                             final TaskCounter taskCounter) {
        super(pathPipe, documentPipe, agentState, taskCounter);
    }

    @Override
    protected DocumentImpl transformSingleton(final Path path) throws IOException {
        return new DocumentImpl(PDDocument.load(path.toFile()));
    }
}
