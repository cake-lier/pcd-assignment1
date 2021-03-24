package it.unibo.pcd.assignment1.wrapper;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFPartImpl implements PDFPart{
    private final PDDocument data;

    public PDFPartImpl(final PDDocument data) {
        this.data = data;
    }

    public PDDocument getData() {
        return data;
    }
}
