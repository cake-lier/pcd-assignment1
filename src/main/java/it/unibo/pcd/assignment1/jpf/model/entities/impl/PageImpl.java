package it.unibo.pcd.assignment1.jpf.model.entities.impl;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;


public class PageImpl implements Page {
    private final String text;

    public PageImpl(final String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
