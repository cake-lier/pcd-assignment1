package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;
import it.unibo.pcd.assignment1.wrapper.Document;
import it.unibo.pcd.assignment1.wrapper.Page;
import it.unibo.pcd.assignment1.wrapper.PageImpl;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DocumentFilterPolicy extends AbstractFilterPolicy<Document, Page> {

    public DocumentFilterPolicy(final Pipe<Document> sourceDocuments,final Pipe<Page> productPages,final SharedAgentState agentState) {
        super(sourceDocuments, productPages,agentState);
    }

    @Override
    protected List<Page> transform(Document document) {
        try {
            final Splitter splitter = new Splitter();
            final PDDocument pdfDocument = document.getData();
            splitter.setStartPage(1);//TODO FAI meglio
            splitter.setSplitAtPage(1);
            final List<PDDocument> pages = splitter.split(pdfDocument);
            System.out.println(pages.size() == pdfDocument.getNumberOfPages());
            return IntStream.rangeClosed(1, pages.size()).<Page>mapToObj(n -> new PageImpl(pages.get(n-1),pdfDocument, n)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
