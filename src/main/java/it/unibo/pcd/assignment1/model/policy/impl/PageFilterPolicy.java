package it.unibo.pcd.assignment1.model.policy.impl;

import it.unibo.pcd.assignment1.model.agent.SharedAgentState;
import it.unibo.pcd.assignment1.model.concurrency.Pipe;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateImpl;
import it.unibo.pcd.assignment1.wrapper.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PageFilterPolicy extends AbstractSingleProductFilterPolicy<String, Update> {
    private static final int SINGLE_PAGE_NUMBER = 1;
    private final Set<String> stopwards;

    public PageFilterPolicy(final Pipe<String> sourcePages,final Pipe<Update> productUpdate,
                            final Set<String> stopwords,final SharedAgentState agentState){
        super(sourcePages, productUpdate,agentState);
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
            System.out.println(Arrays.asList(words));
            return words;
    }
}
