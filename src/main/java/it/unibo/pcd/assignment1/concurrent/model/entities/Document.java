package it.unibo.pcd.assignment1.concurrent.model.entities;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface Document {
    PDDocument getInternalDocument();
}
