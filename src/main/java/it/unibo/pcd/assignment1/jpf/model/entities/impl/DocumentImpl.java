package it.unibo.pcd.assignment1.jpf.model.entities.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Document;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;

import java.util.List;

public class DocumentImpl implements Document {
    private final List<Page> internalDocument;

    public DocumentImpl(final List<Page> internalDocument) {
        this.internalDocument = internalDocument;
    }

    public List<Page> getInternalDocument() {
        return this.internalDocument;
    }
}
