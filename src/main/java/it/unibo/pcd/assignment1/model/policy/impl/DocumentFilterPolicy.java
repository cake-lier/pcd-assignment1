package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;
import it.unibo.pcd.assignment1.wrapper.Document;
import it.unibo.pcd.assignment1.wrapper.Page;
import it.unibo.pcd.assignment1.wrapper.PageImpl;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DocumentFilterPolicy extends AbstractFilterPolicy<Document, String> {

    public DocumentFilterPolicy(final Pipe<Document> sourceDocuments,final Pipe<String> productPages,final SharedAgentState agentState) {
        super(sourceDocuments, productPages,agentState);
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
