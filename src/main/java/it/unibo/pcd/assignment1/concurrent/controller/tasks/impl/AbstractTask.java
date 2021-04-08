package it.unibo.pcd.assignment1.concurrent.controller.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.controller.tasks.Task;

import java.util.Objects;

/**
 * An abstract implementation of the {@link Task} interface, common to all tasks.
 */
public abstract class AbstractTask implements Task {
    private final AgentSuspendedFlag suspendedFlag;
    private final TaskCounter taskCounter;

    /**
     * Default constructor.
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     */
    public AbstractTask(final AgentSuspendedFlag suspendedFlag, final TaskCounter taskCounter) {
        this.suspendedFlag = Objects.requireNonNull(suspendedFlag);
        this.taskCounter = Objects.requireNonNull(taskCounter);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * It executes the proper code that makes the run method, while also returning if this task should be executed again or not.
     * @return whether or not this task should be executed again
     * @throws Exception an exception that the run method might throw during its execution
     */
    protected abstract boolean doRun() throws Exception;

    /**
     * The code to be executed after when it has been decided that this tas should end.
     */
    protected abstract void doEnd();

    /**
     * It returns the flag for checking whether the execution should be suspended or not.
     * @return the flag for checking whether the execution should be suspended or not
     */
    protected AgentSuspendedFlag getSuspendedFlag() {
        return this.suspendedFlag;
    }
}
