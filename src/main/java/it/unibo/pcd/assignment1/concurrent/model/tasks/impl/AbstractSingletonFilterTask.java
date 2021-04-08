package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;

import java.util.Collections;

/**
 * An abstract extension of the {@link AbstractFilterTask} class, common to all "filter" tasks which produce only one
 * output resource after the transformation of an input resource.
 * @param <I> the type of the resources this task takes as input
 * @param <O> the type of the resources this task takes as output
 */
abstract class AbstractSingletonFilterTask<I, O> extends AbstractFilterTask<I, O> {
    /**
     * Default constructor.
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     * @param inputPipe the {@link Pipe} from which getting the input resources to transform
     * @param outputPipe the {@link Pipe} into which putting the transformed output resources
     */
    protected AbstractSingletonFilterTask(final Pipe<I> inputPipe,
                                          final Pipe<O> outputPipe,
                                          final AgentSuspendedFlag suspendedFlag,
                                          final TaskCounter taskCounter) {
        super(suspendedFlag, taskCounter, inputPipe, outputPipe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterable<O> transform(final I input) throws Exception {
        return Collections.singletonList(this.transformSingleton(input));
    }

    /**
     * It transforms an input resource into a single output resource. It should be implemented by the subclasses providing
     * their own behavior.
     * @param input the input resource to process
     * @return the output resource already processed
     * @throws Exception an exception the transformation process might throw
     */
    protected abstract O transformSingleton(final I input) throws Exception;
}
