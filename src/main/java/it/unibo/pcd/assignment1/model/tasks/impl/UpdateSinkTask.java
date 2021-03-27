package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.view.View;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class UpdateSinkTask extends AbstractTask {
    private final Pipe<Update> updates;
    private final Consumer<Update> updatesSink;
    private final Runnable completionNotifier;

    public UpdateSinkTask(final Pipe<Update> updates,
                          final Consumer<Update> updatesSink,
                          final Runnable completionNotifier,
                          final AgentSuspendedFlag suspendedFlag,
                          final TaskCounter ticketManager) {
        super(suspendedFlag, ticketManager);
        this.updates = updates;
        this.updatesSink = Objects.requireNonNull(updatesSink);
        this.completionNotifier = Objects.requireNonNull(completionNotifier);
    }

    @Override
    protected boolean doRun() {
        final Optional<Update> update = this.updates.dequeue();
        update.ifPresent(this.updatesSink);
        return update.isPresent();
    }

    @Override
    protected void doEnd() {
        this.completionNotifier.run();
    }
}
