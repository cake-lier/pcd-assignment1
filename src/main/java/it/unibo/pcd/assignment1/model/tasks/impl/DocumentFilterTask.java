package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;
import it.unibo.pcd.assignment1.wrapper.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentFilterTask extends AbstractFilterTask<Document, String> {
    public DocumentFilterTask(final PipeConnector<Document, String> connector, final AgentSuspendedFlag agentState, final AgentTicketManager ticketManager) {
        super(connector, agentState,ticketManager);
    }

    @Override
    protected List<String> transform(final Document document) throws IOException {
        final PDFTextStripper stripper = new PDFTextStripper();
        final PDDocument pdfDocument = document.getData();
        final List<String> pages = new ArrayList<>();
        for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
            stripper.setStartPage(page);
            stripper.setEndPage(page);
            pages.add(stripper.getText(pdfDocument));
        }
        return pages;
    }
}
