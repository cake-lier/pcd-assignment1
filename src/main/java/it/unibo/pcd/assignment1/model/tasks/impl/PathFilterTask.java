package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;
import it.unibo.pcd.assignment1.wrapper.Document;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;

public class PathFilterTask extends AbstractSingletonFilterTask<Path, Document> {
    public PathFilterTask(final PipeConnector<Path, Document> pipeConnector, final AgentSuspendedFlag agentState) {
        super(pipeConnector, agentState);
    }

    @Override
    protected Document transformSingleton(final Path path) throws IOException {
        return new Document(PDDocument.load(path.toFile()));
    }
}
