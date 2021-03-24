package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.pipe.Pipe;
import it.unibo.pcd.assignment1.model.concurrency.pipe.PipeConnector;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateImpl;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PageFilterPolicy extends AbstractSingleProductFilterPolicy<String, Update> {
    private static final int SINGLE_PAGE_NUMBER = 1;
    private final Set<String> stopwards;

    public PageFilterPolicy(final PipeConnector<String,Update> pipeConnector,final Set<String> stopwords, final SharedAgentState agentState){
        super(pipeConnector,agentState);
        this.stopwards = stopwords;
    }

    @Override
    protected Update transformSingleValue(String page) {
        return this.wordsToUpdate(this.exctractWords(page));
    }

    private UpdateImpl wordsToUpdate(final String[] words){
        return new UpdateImpl(Arrays.stream(words)
                .map(String::toLowerCase)
                .filter(w -> !this.stopwards.contains(w))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting())),
                words.length);
    }

    private String[] exctractWords(final String page){
            final String[] words = Pattern.compile("\\W+").split(page);
            return words;
    }
}
