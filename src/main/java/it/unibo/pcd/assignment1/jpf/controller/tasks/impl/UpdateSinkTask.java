package it.unibo.pcd.assignment1.jpf.controller.tasks.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Update;
import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Objects;
import java.util.Optional;

public class UpdateSinkTask extends AbstractTask {
    private final WordCounter wordCounter;
    private final int expectedWordNumber;
    private final String expectedWord;
    private Optional<Update> lastUpdate;

    public UpdateSinkTask(final WordCounter wordCounter,
                          final TaskCounter taskCounter,
                          final int expectedWordNumber,
                          final String expectedWord) {
        super(taskCounter);
        this.wordCounter = Objects.requireNonNull(wordCounter);
        this.expectedWordNumber = expectedWordNumber;
        this.expectedWord = Objects.requireNonNull(expectedWord);
        this.lastUpdate = Optional.empty();
    }

    @Override
    protected boolean doRun() {
        Optional<Update> update = this.wordCounter.dequeue();
        if (update.isPresent()) {
            this.lastUpdate = update;
            return true;
        }
        return false;
    }

    @Override
    protected void doEnd() {
        assert this.lastUpdate.isPresent();
        final Update update = this.lastUpdate.get();
        assert update.getProcessedWords() == this.expectedWordNumber;
        assert update.getFrequencies().size() == 1;
        assert update.getFrequencies().containsKey(this.expectedWord);
        assert update.getFrequencies().get(this.expectedWord) == this.expectedWordNumber;
    }
}
