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

/**
 * A {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} for transforming {@link Document}s into {@link Page}s.
 */
class DocumentFilterTask extends AbstractFilterTask<Document, Page> {
    /**
     * Default constructor.
     * @param documentPipe the {@link Pipe} from which dequeueing the {@link Document}s to be transformed
     * @param pagePipe the {@link Pipe} in which enqueueing the transformed {@link Page}s
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     */
    protected DocumentFilterTask(final Pipe<Document> documentPipe,
                                 final Pipe<Page> pagePipe,
                                 final AgentSuspendedFlag suspendedFlag,
                                 final TaskCounter taskCounter) {
        super(suspendedFlag, taskCounter, documentPipe, pagePipe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DocumentFilterTask";
    }

    /**
     * {@inheritDoc}
     */
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
