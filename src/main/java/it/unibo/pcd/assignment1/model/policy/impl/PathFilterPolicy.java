package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;
import it.unibo.pcd.assignment1.wrapper.Document;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;

public class PathFilterPolicy extends AbstractSingleProductFilterPolicy<Path, Document> {

    public PathFilterPolicy(final PipeConnector<Path,Document> pipeConnector,
                            final SharedAgentState agentState) {
        super(pipeConnector,agentState);
    }

    @Override
    protected Document transformSingleValue(Path path) {
        try {
            return new Document(PDDocument.load(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
            //TODO what to do? (lol)
        }
    }
}
