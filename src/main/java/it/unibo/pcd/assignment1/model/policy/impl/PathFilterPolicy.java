package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;
import it.unibo.pcd.assignment1.wrapper.Document;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;

public class PathFilterPolicy extends AbstractSingleProductFilterPolicy<Path, Document> {

    public PathFilterPolicy(final Pipe<Path> sourcePaths,final Pipe<Document> productDocuments,
                            final SharedAgentState agentState) {
        super(sourcePaths, productDocuments,agentState);
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
