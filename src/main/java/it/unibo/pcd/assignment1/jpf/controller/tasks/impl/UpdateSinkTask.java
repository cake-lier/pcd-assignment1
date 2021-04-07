package it.unibo.pcd.assignment1.jpf.controller.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Objects;

public class UpdateSinkTask extends AbstractTask {
    private final WordCounter wordCounter;

    public UpdateSinkTask(final WordCounter wordCounter,
                          final TaskCounter ticketManager) {
        super(ticketManager);
        this.wordCounter = Objects.requireNonNull(wordCounter);
    }

    @Override
    protected boolean doRun(){
        return this.wordCounter.dequeue().isPresent();
    }

    @Override
    protected void doEnd() {
        System.out.println("End");
    }
}
