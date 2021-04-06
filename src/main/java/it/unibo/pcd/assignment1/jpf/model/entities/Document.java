package it.unibo.pcd.assignment1.jpf.model.entities;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.List;

public interface Document {
    List<Page> getInternalDocument();
}
