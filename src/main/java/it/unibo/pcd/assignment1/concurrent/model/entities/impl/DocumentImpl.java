package it.unibo.pcd.assignment1.concurrent.model.entities.impl;

import it.unibo.pcd.assignment1.concurrent.model.entities.Document;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.Objects;

public class DocumentImpl implements Document {
    private final PDDocument internalDocument;

    public DocumentImpl(final PDDocument internalDocument) {
        this.internalDocument = internalDocument;
    }

    public PDDocument getInternalDocument() {
        return this.internalDocument;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return internalDocument.equals(((DocumentImpl) o).internalDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.internalDocument);
    }

    @Override
    public String toString() {
        return "DocumentImpl{internalDocument=" + this.internalDocument + '}';
    }
}
