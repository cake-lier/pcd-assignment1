package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.shared.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.shared.StopwordsSet;
import it.unibo.pcd.assignment1.model.tasks.TaskCounter;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.model.updates.UpdateImpl;
import it.unibo.pcd.assignment1.model.entities.Page;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class PageFilterTask extends AbstractSingletonFilterTask<Page, Update> {
    private final StopwordsSet stopwords;

    protected PageFilterTask(final Pipe<Page> pagePipe,
                             final Pipe<Update> updatePipe,
                             final StopwordsSet stopwords,
                             final AgentSuspendedFlag agentState,
                             final TaskCounter taskCounter) {
        super(pagePipe, updatePipe, agentState, taskCounter);
        this.stopwords = Objects.requireNonNull(stopwords);
    }

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
