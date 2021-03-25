package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.view.View;

import java.util.Optional;

public class UpdateSinkTask extends AbstractTask{
    private final Pipe<Update> updates;
    private final View view;

    public UpdateSinkTask(final View view, final Pipe<Update> updates,
                          final AgentSuspendedFlag suspendedFlag,
                          final AgentTicketManager ticketManager) {
        super(suspendedFlag, ticketManager);
        this.updates = updates;
        this.view = view;
    }

    @Override
    protected boolean doAction() throws Exception {
        final Optional<Update> update = this.updates.dequeue();
        if (!update.isPresent()) {
            return false;
        }
        this.view.displayProgress(update.get().getFrequencies(), update.get().getProcessedWords());
        return true;
    }

    @Override
    protected void doEnd() {
        this.view.displayCompletion();
    }
}
