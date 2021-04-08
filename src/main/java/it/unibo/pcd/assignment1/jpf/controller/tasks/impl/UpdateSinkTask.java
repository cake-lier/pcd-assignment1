package it.unibo.pcd.assignment1.jpf.controller.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Update;
import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Objects;
import java.util.Optional;

public class UpdateSinkTask extends AbstractTask {
    private final WordCounter wordCounter;
    private final int wordNumberExpectation;

    private long processedWord;

    public UpdateSinkTask(final WordCounter wordCounter,
                          final TaskCounter ticketManager,
                          final int wordNumberExpectation) {
        super(ticketManager);
        this.wordCounter = Objects.requireNonNull(wordCounter);
        this.wordNumberExpectation = wordNumberExpectation;
        this.processedWord = 0;
    }

    @Override
    protected boolean doRun(){
        Optional<Update> update = this.wordCounter.dequeue();
        if(update.isPresent()){
            this.processedWord = update.get().getProcessedWords();
            return true;
        }
        return false;
    }

    @Override
    protected void doEnd() {
        assert this.processedWord == this.wordNumberExpectation;
    }
}
