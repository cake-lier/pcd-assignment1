package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;
import it.unibo.pcd.assignment1.wrapper.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentFilterPolicy extends AbstractFilterPolicy<Document, String> {

    public DocumentFilterPolicy(final PipeConnector<Document,String> connector, final SharedAgentState agentState) {
        super(connector, agentState);
    }

    @Override
    protected List<String> transform(Document document) {
        try {
            final PDFTextStripper stripper = new PDFTextStripper();
            final PDDocument pdfDocument = document.getData();
            final List<String> pages = new ArrayList<>();
            for(int n = 1; n<=pdfDocument.getNumberOfPages(); n++){
                stripper.setStartPage(n);
                stripper.setEndPage(n);
                pages.add(stripper.getText(pdfDocument));
            }
            return pages;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
