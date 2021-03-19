package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.concurrency.CloseableResourceQueueImpl;
import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateImpl;

import java.util.*;

public class WordCounter extends CloseableResourceQueueImpl<Update> {
    private final Map<String, Long> frequencies;
    private int processedWords;

    public WordCounter() {
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WordCounter that = (WordCounter) o;
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
