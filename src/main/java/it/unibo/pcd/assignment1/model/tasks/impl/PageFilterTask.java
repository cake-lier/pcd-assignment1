package it.unibo.pcd.assignment1.model.tasks.impl;

import it.unibo.pcd.assignment1.controller.agents.AgentSuspendedFlag;
import it.unibo.pcd.assignment1.controller.agents.AgentTicketManager;
import it.unibo.pcd.assignment1.model.pipes.Pipe;
import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.model.updates.UpdateImpl;
import it.unibo.pcd.assignment1.wrapper.Page;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PageFilterTask extends AbstractSingletonFilterTask<Page, Update> {
    private final Set<String> stopwords;

    public PageFilterTask(final Pipe<Page> pagePipe,
                          final Pipe<Update> updatePipe,
                          final Set<String> stopwords,
                          final AgentSuspendedFlag agentState,
                          final AgentTicketManager ticketManager) {
        super(pagePipe,updatePipe, agentState,ticketManager);
        this.stopwords = new HashSet<>(stopwords);
    }

    @Override
    protected Update transformSingleton(final Page page) {
        final String[] words = Pattern.compile("\\W+").split(page.getText());
        return new UpdateImpl(Arrays.stream(words)
                                    .map(String::toLowerCase)
                                    .filter(w -> !this.stopwords.contains(w))
                                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())),
                              words.length);
    }

}
