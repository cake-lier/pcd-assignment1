package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.PageImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentFilterTask extends AbstractFilterTask<Document, Page> {
    public DocumentFilterTask(final Pipe<Document> documentPipe,
                                 final Pipe<Page> pagePipe,
                                 final TaskCounter taskCounter) {
        super(taskCounter, documentPipe, pagePipe);
    }

    @Override
    protected List<Page> transform(final Document document){
        return document.getInternalDocument();
    }
}
