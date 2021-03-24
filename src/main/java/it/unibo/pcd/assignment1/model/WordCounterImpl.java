package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateImpl;

import java.util.*;
import java.util.stream.Collectors;

public class WordCounterImpl extends FilterPipeImpl<Update> {
    private final int wordsNumber;
    private final Map<String, Long> frequencies;
    private int processedWords;

    public WordCounterImpl(final int wordsNumber) {
        this.wordsNumber = wordsNumber;
        this.frequencies = new HashMap<>();
        this.processedWords = 0;
    }

    @Override
    protected void doEnqueue(final Update element) {
        element.getFrequencies().forEach((k, v) -> this.frequencies.merge(k, v, Long::sum));
        this.processedWords += element.getProcessedWords();
        super.doEnqueue(new UpdateImpl(this.frequencies, this.processedWords));
    }

    @Override
    protected Optional<Update> doDequeue() {
        return super.doDequeue().map(u -> new UpdateImpl(
            u.getFrequencies()
             .entrySet()
             .parallelStream()
             .sorted(Collections.<Map.Entry<String, Long>>reverseOrder(Map.Entry.comparingByValue())
                                .thenComparing(Map.Entry.comparingByKey()))
             .limit(this.wordsNumber)
             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, LinkedHashMap::new)),
            u.getProcessedWords()
        ));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WordCounterImpl that = (WordCounterImpl) o;
        return this.processedWords == that.processedWords
               && this.frequencies.equals(that.frequencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.frequencies, this.processedWords);
    }

    @Override
    public String toString() {
        return "WordCounterImpl{frequencies=" + this.frequencies + ", processedWords=" + this.processedWords + '}';
    }
}
