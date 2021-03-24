package it.unibo.pcd.assignment1.model.concurrency;

import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateImpl;

import java.util.*;
import java.util.stream.Collectors;

public class WordCounter extends BoundedPipe<Update>{
    private final int wordsNumber;
    private final Map<String, Long> frequencies;
    private int processedWords;

    public WordCounter(int maxNumberOfElements,int wordsNumber) {
        super(maxNumberOfElements);
        this.wordsNumber = wordsNumber;
        this.frequencies = new HashMap<>();
    }

    @Override
    protected void doEnqueue(Update element) {
        element.getFrequencies().forEach((k, v) -> this.frequencies.merge(k, v, Long::sum));
        this.processedWords += element.getProcessedWords();
        final Map<String,Long> topWords = this.frequencies.entrySet().parallelStream()
                .sorted(Collections.<Map.Entry<String, Long>>reverseOrder(Map.Entry.comparingByValue())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(this.wordsNumber)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, LinkedHashMap::new));
        super.doEnqueue(new UpdateImpl(topWords, this.processedWords));
    }
}
