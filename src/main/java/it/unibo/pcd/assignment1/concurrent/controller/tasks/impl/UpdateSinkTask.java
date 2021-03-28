package it.unibo.pcd.assignment1.concurrent.controller.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.entities.Update;
import it.unibo.pcd.assignment1.concurrent.view.View;

import java.util.Objects;
import java.util.Optional;

public class UpdateSinkTask extends AbstractTask {
    private static final int MILLIS_BETWEEN_FRAMES = Math.round(1000.0f / 60.0f);

    private final WordCounter wordCounter;
    private final View view;
    private Optional<Long> initialTime;

    public UpdateSinkTask(final WordCounter wordCounter,
                          final View view,
                          final AgentSuspendedFlag suspendedFlag,
                          final TaskCounter ticketManager) {
        super(suspendedFlag, ticketManager);
        this.wordCounter = Objects.requireNonNull(wordCounter);
        this.view = Objects.requireNonNull(view);
        this.initialTime = Optional.empty();
    }

    @Override
    protected boolean doRun() throws Exception {
        if (!this.initialTime.isPresent()) {
            this.initialTime = Optional.of(System.currentTimeMillis());
        }
        final Optional<Update> update = this.wordCounter.dequeue();
        if (!update.isPresent()) {
            return false;
        }
        final long timeRemaining = MILLIS_BETWEEN_FRAMES - (System.currentTimeMillis() - this.initialTime.get());
        if (timeRemaining > 0) {
            Thread.sleep(timeRemaining);
            final Update latestUpdate = this.wordCounter.drain().orElseGet(update::get);
            this.view.displayProgress(latestUpdate.getFrequencies(), latestUpdate.getProcessedWords());
        } else {
            final Update currentUpdate = update.get();
            this.view.displayProgress(currentUpdate.getFrequencies(), currentUpdate.getProcessedWords());
        }
        this.initialTime = Optional.empty();
        return true;
    }

    @Override
    protected void doEnd() {
        this.view.displayCompletion();
    }
}
