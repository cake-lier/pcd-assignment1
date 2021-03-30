package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.entities.Document;
import it.unibo.pcd.assignment1.concurrent.model.entities.impl.DocumentImpl;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;

class PathFilterTask extends AbstractSingletonFilterTask<Path, Document> {
    protected PathFilterTask(final Pipe<Path> pathPipe,
                             final Pipe<Document> documentPipe,
                             final AgentSuspendedFlag agentState,
                             final TaskCounter taskCounter) {
        super(pathPipe, documentPipe, agentState, taskCounter);
    }

    @Override
    public String toString() {
        return "PathFilterTask";
    }

    @Override
    protected Document transformSingleton(final Path path) throws IOException {
        return new DocumentImpl(PDDocument.load(path.toFile()));
    }
}
