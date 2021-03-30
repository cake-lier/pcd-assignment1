package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.entities.Document;
import it.unibo.pcd.assignment1.concurrent.model.entities.Page;
import it.unibo.pcd.assignment1.concurrent.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DocumentFilterTask extends AbstractFilterTask<Document, Page> {
    protected DocumentFilterTask(final Pipe<Document> documentPipe,
                                 final Pipe<Page> pagePipe,
                                 final AgentSuspendedFlag agentState,
                                 final TaskCounter taskCounter) {
        super(agentState, taskCounter, documentPipe, pagePipe);
    }

    @Override
    public String toString() {
        return "DocumentFilterTask";
    }

    @Override
    protected List<Page> transform(final Document document) throws IOException {
        final PDFTextStripper stripper = new PDFTextStripper();
        final PDDocument pdfDocument = document.getInternalDocument();
        final List<Page> pages = new ArrayList<>();
        for (int page = 1; page <= pdfDocument.getNumberOfPages(); page++) {
            stripper.setStartPage(page);
            stripper.setEndPage(page);
            pages.add(new PageImpl(stripper.getText(pdfDocument)));
        }
        pdfDocument.close();
        return pages;
    }
}
