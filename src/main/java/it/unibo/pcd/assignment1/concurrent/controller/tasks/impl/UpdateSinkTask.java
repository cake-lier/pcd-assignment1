package it.unibo.pcd.assignment1.concurrent.controller.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.entities.Update;
import it.unibo.pcd.assignment1.concurrent.view.View;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} for collecting the results of the computation, the
 * {@link Update}s, and publish them to the View component.
 */
public class UpdateSinkTask extends AbstractTask {
    private static final int MILLIS_BETWEEN_FRAMES = Math.round(1000.0f / 60.0f);

    private final WordCounter wordCounter;
    private final View view;
    private Optional<Long> initialTime;

    /**
     * Default constructor.
     * @param wordCounter the {@link it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe} from which getting the {@link Update}s
     * @param view the View component this task should publish its {@link Update}s onto
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     */
    public UpdateSinkTask(final WordCounter wordCounter,
                          final View view,
                          final AgentSuspendedFlag suspendedFlag,
                          final TaskCounter taskCounter) {
        super(suspendedFlag, taskCounter);
        this.wordCounter = Objects.requireNonNull(wordCounter);
        this.view = Objects.requireNonNull(view);
        this.initialTime = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UpdateSinkTask";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEnd() {
        this.view.displayCompletion();
    }
}
