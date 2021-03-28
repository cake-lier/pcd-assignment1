package it.unibo.pcd.assignment1.concurrent.controller.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;

import java.util.Objects;

public abstract class AbstractTask implements Task {
    private final AgentSuspendedFlag suspendedFlag;
    private final TaskCounter taskCounter;

    public AbstractTask(final AgentSuspendedFlag suspendedFlag, final TaskCounter taskCounter) {
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
}
