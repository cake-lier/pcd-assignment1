package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.controller.tasks.impl.AbstractTask;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;

import java.util.Objects;
import java.util.Optional;

/**
 * An abstract extension of the {@link AbstractTask} class, common to all "filter" tasks.
 * @param <I> the type of the resources this task takes as input
 * @param <O> the type of the resources this task takes as output
 */
abstract class AbstractFilterTask<I, O> extends AbstractTask {
    private final Pipe<I> inputPipe;
    private final Pipe<O> outputPipe;

    /**
     * Default constructor.
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     * @param inputPipe the {@link Pipe} from which getting the input resources to transform
     * @param outputPipe the {@link Pipe} into which putting the transformed output resources
     */
    protected AbstractFilterTask(final AgentSuspendedFlag suspendedFlag,
                                 final TaskCounter taskCounter,
                                 final Pipe<I> inputPipe,
                                 final Pipe<O> outputPipe) {
        super(suspendedFlag, taskCounter);
        this.inputPipe = Objects.requireNonNull(inputPipe);
        this.outputPipe = Objects.requireNonNull(outputPipe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doRun() throws Exception {
        final Optional<I> input = this.inputPipe.dequeue();
        if (input.isPresent()) {
            this.transform(input.get())
                .forEach(o -> {
                    this.getSuspendedFlag().check();
                    this.outputPipe.enqueue(o);
                });
        }
        return input.isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEnd() {
        this.outputPipe.close();
    }

    /**
     * It transforms an input resource into a sequence of output resources. It should be implemented by the subclasses providing
     * their own behavior.
     * @param input the input resource to process
     * @return a sequence of output resources already processed
     * @throws Exception an exception the transformation process might throw
     */
    protected abstract Iterable<O> transform(final I input) throws Exception;
}
