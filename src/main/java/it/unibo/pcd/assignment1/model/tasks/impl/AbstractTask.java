package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.controller.agents.Task;

import java.util.Objects;

abstract class AbstractTask implements Task {
    private final AgentSuspendedFlag suspendedFlag;
    private final TaskCounter taskCounter;

    protected AbstractTask(final AgentSuspendedFlag suspendedFlag, final TaskCounter taskCounter) {
        this.suspendedFlag = Objects.requireNonNull(suspendedFlag);
        this.taskCounter = Objects.requireNonNull(taskCounter);
    }

    @Override
    public final void run() throws Exception {
        this.taskCounter.incrementOfType(this.getClass());
        do {
            this.suspendedFlag.check();
        } while (this.doRun());
        if (this.taskCounter.decrementOfType(this.getClass())) {
            this.doEnd();
        }
    }

    protected abstract boolean doRun() throws Exception;

    protected abstract void doEnd();

    protected AgentSuspendedFlag getSuspendedFlag() {
        return this.suspendedFlag;
    }

    protected TaskCounter getTaskCounter() {
        return this.taskCounter;
    }
}
