package it.unibo.pcd.assignment1.jpf.model.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.impl.UpdateImpl;
import it.unibo.pcd.assignment1.jpf.model.entities.Page;
import it.unibo.pcd.assignment1.jpf.model.entities.Update;
import it.unibo.pcd.assignment1.jpf.model.pipes.Pipe;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.*;
import java.util.regex.Pattern;

public class PageFilterTask extends AbstractSingletonFilterTask<Page, Update> {

    public PageFilterTask(final Pipe<Page> pagePipe, final Pipe<Update> updatePipe, final TaskCounter taskCounter) {
        super(pagePipe, updatePipe, taskCounter);
    }

    @Override
    protected Update transformSingleton(final Page page) {
        final String[] words = Pattern.compile("\\W+").split(page.getText());
        final Map<String, Long> updateData = new HashMap<>();
        //noinspection Convert2MethodRef
        Arrays.asList(words).forEach(w -> updateData.merge(w, 1L, (o, n) -> o + n));
        return new UpdateImpl(updateData, words.length);
    }
}
