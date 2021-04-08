package it.unibo.pcd.assignment1.concurrent.model.pipes.impl;

import it.unibo.pcd.assignment1.concurrent.model.pipes.WordCounter;
import it.unibo.pcd.assignment1.concurrent.model.entities.Update;
import it.unibo.pcd.assignment1.concurrent.model.entities.impl.UpdateImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link WordCounter} interface.
 */
public class WordCounterImpl extends BoundedPipe<Update> implements WordCounter {
    private final int wordsNumber;
    private final Map<String, Long> frequencies;
    private int processedWords;

    /**
     * Default constructor.
     * @param maxNumberOfElements the maximum number of elements this pipe can hold at the same time
     * @param wordsNumber the maximum number of most frequent words an {@link Update} should show
     */
    public WordCounterImpl(final int maxNumberOfElements, final int wordsNumber) {
        super(maxNumberOfElements);
        this.wordsNumber = wordsNumber;
        this.frequencies = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEnqueue(final Update element) {
        element.getFrequencies().forEach((k, v) -> this.frequencies.merge(k, v, Long::sum));
        this.processedWords += element.getProcessedWords();
        super.doEnqueue(new UpdateImpl(
            this.frequencies.entrySet()
                            .parallelStream()
                            .sorted(Collections.<Map.Entry<String, Long>>reverseOrder(Map.Entry.comparingByValue())
                                               .thenComparing(Map.Entry.comparingByKey()))
                            .limit(this.wordsNumber)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, LinkedHashMap::new)),
            this.processedWords
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Update> drain() {
        this.getLock().lock();
        try {
            while (!this.isEmpty()) {
                final Optional<Update> lastUpdate = this.dequeue();
                if (this.isEmpty()) {
                    return lastUpdate;
                }
            }
            return Optional.empty();
        } finally {
            this.getLock().unlock();
        }
    }
}
