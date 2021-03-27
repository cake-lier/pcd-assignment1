package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.pipes.impl.WordCounter;
import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.updates.Update;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class UpdateSinkTask extends AbstractTask {
    private static final int MILLIS_BETWEEN_FRAMES = Math.round(1000.0f / 60.0f);

    private final WordCounter wordCounter;
    private final Consumer<Update> updatesSink;
    private final Runnable completionNotifier;
    private Optional<Long> initialTime;

    public UpdateSinkTask(final WordCounter wordCounter,
                          final Consumer<Update> updatesSink,
                          final Runnable completionNotifier,
                          final AgentSuspendedFlag suspendedFlag,
                          final TaskCounter ticketManager) {
        super(suspendedFlag, ticketManager);
        this.wordCounter = Objects.requireNonNull(wordCounter);
        this.updatesSink = Objects.requireNonNull(updatesSink);
        this.completionNotifier = Objects.requireNonNull(completionNotifier);
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
            this.updatesSink.accept(this.wordCounter.drain().orElseGet(update::get));
        } else {
            this.updatesSink.accept(update.get());
        }
        this.initialTime = Optional.empty();
        return true;
    }

    @Override
    protected void doEnd() {
        this.completionNotifier.run();
    }
}
