package it.unibo.pcd.assignment1.model.pipes.impl;

import it.unibo.pcd.assignment1.model.updates.Update;
import it.unibo.pcd.assignment1.model.updates.UpdateImpl;

import java.util.*;
import java.util.stream.Collectors;

public class WordCounter extends BoundedPipe<Update> {
    private final int wordsNumber;
    private final Map<String, Long> frequencies;
    private int processedWords;

    public WordCounter(int maxNumberOfElements,int wordsNumber) {
        super(maxNumberOfElements);
        this.wordsNumber = wordsNumber;
        this.frequencies = new HashMap<>();
    }

    @Override
    protected void doEnqueue(final Update element) {
        element.getFrequencies().forEach((k, v) -> this.frequencies.merge(k, v, Long::sum));
        this.processedWords += element.getProcessedWords();
        final Map<String, Long> topWords
            = this.frequencies.entrySet()
                              .parallelStream()
                              .sorted(Collections.<Map.Entry<String, Long>>reverseOrder(Map.Entry.comparingByValue())
                                                 .thenComparing(Map.Entry.comparingByKey()))
                              .limit(this.wordsNumber)
                              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, LinkedHashMap::new));
        super.doEnqueue(new UpdateImpl(topWords, this.processedWords));
    }

    public Optional<Update> drain() {
        this.getLock().lock();
        try {
            while (this.getSize() != 0) {
                final Optional<Update> lastUpdate = this.dequeue();
                if (this.getSize() == 0) {
                    return lastUpdate;
                }
            }
            return Optional.empty();
        } finally {
            this.getLock().unlock();
        }
    }
}
