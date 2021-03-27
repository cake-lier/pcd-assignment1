package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.entities.DocumentImpl;
import it.unibo.pcd.assignment1.model.entities.Page;
import it.unibo.pcd.assignment1.model.entities.PageImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DocumentFilterTask extends AbstractFilterTask<DocumentImpl, Page> {
    protected DocumentFilterTask(final Pipe<DocumentImpl> documentPipe,
                                 final Pipe<Page> pagePipe,
                                 final AgentSuspendedFlag agentState,
                                 final TaskCounter taskCounter) {
        super(agentState, taskCounter, documentPipe, pagePipe);
    }

    @Override
    protected List<Page> transform(final DocumentImpl document) throws IOException {
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
