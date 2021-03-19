package it.unibo.pcd.assignment1.model;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface Page {
    PDDocument getDocument();

    int getPageIndex();
}
