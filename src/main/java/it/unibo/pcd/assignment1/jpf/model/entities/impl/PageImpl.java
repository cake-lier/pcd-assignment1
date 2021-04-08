package it.unibo.pcd.assignment1.jpf.model.entities.impl;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;

import java.util.Objects;


public class PageImpl implements Page {
    private final String text;

    public PageImpl(final String text) {
        this.text = Objects.requireNonNull(text);
    }

    @Override
    public String getText() {
        return this.text;
    }
}
