package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.tasks.Task;

import java.util.Objects;

public abstract class AbstractTask implements Task {
    private final AgentSuspendedFlag suspendedFlag;
    private final AgentTicketManager ticketManager;

    public AbstractTask(final AgentSuspendedFlag suspendedFlag, final AgentTicketManager ticketManager) {
        this.suspendedFlag = Objects.requireNonNull(suspendedFlag);
        this.ticketManager = Objects.requireNonNull(ticketManager);
    }

    @Override
    public final void run() throws Exception {
        this.ticketManager.incrementAgentOfType(this.getClass());
        while (true) {
            this.suspendedFlag.check();
            final boolean runAgain = this.doAction();
            if(!runAgain){
                break;
            }
        }
        this.onEnd();
    }

    @Override
    public final void onEnd() throws Exception {
        if(this.ticketManager.decrementAgentOfType(this.getClass())){
            this.doEnd();
        }
    }

    abstract protected boolean doAction() throws Exception;
    abstract protected void doEnd();

    protected AgentSuspendedFlag getSuspendedFlag() {
        return this.suspendedFlag;
    }
    protected AgentTicketManager getTicketManager() { return  this.ticketManager;}
}
