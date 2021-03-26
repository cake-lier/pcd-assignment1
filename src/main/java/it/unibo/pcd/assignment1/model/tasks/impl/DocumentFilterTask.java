package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.wrapper.Document;
import it.unibo.pcd.assignment1.wrapper.Page;
import it.unibo.pcd.assignment1.wrapper.PageImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentFilterTask extends AbstractFilterTask<Document, Page> {
    public DocumentFilterTask(final Pipe<Document> documentPipe, final Pipe<Page> pagePipe, final AgentSuspendedFlag agentState, final AgentTicketManager ticketManager) {
        super(documentPipe,pagePipe, agentState,ticketManager);
    }

    @Override
    protected List<Page> transform(final Document document) throws IOException {
        final PDFTextStripper stripper = new PDFTextStripper();
        final PDDocument pdfDocument = document.getData();
        final List<Page> pages = new ArrayList<>();
        for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
            stripper.setStartPage(page);
            stripper.setEndPage(page);
            pages.add(new PageImpl(stripper.getText(pdfDocument)));
        }
        return pages;
    }
}
