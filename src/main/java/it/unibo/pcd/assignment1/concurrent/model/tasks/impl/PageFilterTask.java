package it.unibo.pcd.assignment1.concurrent.model.tasks.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.Pipe;
import it.unibo.pcd.assignment1.concurrent.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.concurrent.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.concurrent.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.concurrent.model.entities.Update;
import it.unibo.pcd.assignment1.concurrent.model.entities.impl.UpdateImpl;
import it.unibo.pcd.assignment1.concurrent.model.entities.Page;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A {@link it.unibo.pcd.assignment1.concurrent.controller.tasks.Task} for transforming {@link Page}s into {@link Update}s.
 */
class PageFilterTask extends AbstractSingletonFilterTask<Page, Update> {
    private final StopwordsSet stopwords;

    /**
     * Default constructor.
     * @param pagePipe the {@link Pipe} from which dequeueing the {@link Page}s to be transformed
     * @param updatePipe the {@link Pipe} in which enqueueing the transformed {@link Update}s
     * @param stopwords the {@link StopwordsSet} to populate
     * @param suspendedFlag the flag for checking whether the execution should be suspended or not
     * @param taskCounter the counter to which register for notifying a new task of this type
     */
    protected PageFilterTask(final Pipe<Page> pagePipe,
                             final Pipe<Update> updatePipe,
                             final StopwordsSet stopwords,
                             final AgentSuspendedFlag suspendedFlag,
                             final TaskCounter taskCounter) {
        super(pagePipe, updatePipe, suspendedFlag, taskCounter);
        this.stopwords = Objects.requireNonNull(stopwords);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PageFilterTask";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Update transformSingleton(final Page page) {
        final String[] words = Pattern.compile("\\W+").split(page.getText());
        final Set<String> stopwordsSet = this.stopwords.get();
        return new UpdateImpl(Arrays.stream(words)
                                    .map(String::toLowerCase)
                                    .filter(w -> !stopwordsSet.contains(w))
                                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())),
                              words.length);
    }
}
