package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.model.pipes.PipeConnector;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.model.updates.UpdateImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PageFilterTask extends AbstractSingletonFilterTask<String, Update> {
    private final Set<String> stopwords;

    public PageFilterTask(final PipeConnector<String, Update> pipeConnector,
                          final Set<String> stopwords,
                          final AgentSuspendedFlag agentState) {
        super(pipeConnector, agentState);
        this.stopwords = new HashSet<>(stopwords);
    }

    @Override
    protected Update transformSingleton(final String page) {
        final String[] words = Pattern.compile("\\W+").split(page);
        return new UpdateImpl(Arrays.stream(words)
                                    .map(String::toLowerCase)
                                    .filter(w -> !this.stopwords.contains(w))
                                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())),
                              words.length);
    }
}
