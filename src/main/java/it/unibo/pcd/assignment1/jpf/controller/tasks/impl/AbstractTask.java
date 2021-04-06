package it.unibo.pcd.assignment1.jpf.controller.tasks.impl;

import it.unibo.pcd.assignment1.jpf.controller.tasks.Task;
import it.unibo.pcd.assignment1.jpf.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.jpf.model.tasks.TaskCounter;

import java.util.Objects;

public abstract class AbstractTask implements Task {
    private final TaskCounter taskCounter;

    public AbstractTask(final TaskCounter taskCounter) {
        this.taskCounter = Objects.requireNonNull(taskCounter);
    }

    @Override
    public final void run() {
        this.taskCounter.incrementOfType(this.getClass());
        while (this.doRun());
        if (this.taskCounter.decrementOfType(this.getClass())) {
            this.doEnd();
        }
    }

    protected abstract boolean doRun();

    protected abstract void doEnd();
}
