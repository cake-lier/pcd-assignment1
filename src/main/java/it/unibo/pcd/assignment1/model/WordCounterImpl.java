package it.unibo.pcd.assignment1.model;

import it.unibo.pcd.assignment1.model.update.Update;
import it.unibo.pcd.assignment1.model.update.UpdateFactory;

import java.util.*;
import java.util.stream.Collectors;

public class WordCounterImpl implements WordCounter {
    private final int limitNumber;
    private final Map<String, Integer> frequencies;
    private int processedWords;
    private boolean suspend;

    public WordCounterImpl(final int limitNumber) {
        this.limitNumber = limitNumber;
        this.frequencies = new HashMap<>();
        this.processedWords = 0;
        this.suspend = false;
    }

    @Override
    public synchronized void countWord(final String word, final boolean isToAdd) {
        while (this.suspend) {  //TODO: where? Is this a monitor?
            try {
                this.wait();
            } catch (final InterruptedException ignored) {}
        }
        if (isToAdd) {
            this.frequencies.merge(word, 1, Integer::sum);
        }
        this.processedWords++;
    }

    @Override
    public synchronized void suspend() {
        this.suspend = true;
    }

    @Override
    public synchronized void resume() {
        this.suspend = false;
        this.notifyAll();
    }

    @Override
    public synchronized Update getCurrentState() {
        return UpdateFactory.of(
            this.frequencies.entrySet()
                            .stream()
                            .sorted(Collections.<Map.Entry<String, Integer>>reverseOrder(Map.Entry.comparingByValue())
                                               .thenComparing(Map.Entry.comparingByKey()))
                            .limit(this.limitNumber)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, LinkedHashMap::new)),
            this.processedWords
        );
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
        return this.limitNumber == that.limitNumber
               && this.processedWords == that.processedWords
               && this.suspend == that.suspend
               && this.frequencies.equals(that.frequencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.limitNumber, this.frequencies, this.processedWords, this.suspend);
    }

    @Override
    public String toString() {
        return "WordCounterImpl{limitNumber=" + this.limitNumber + ", frequencies=" + this.frequencies + ", processedWords="
               + this.processedWords + ", suspend=" + this.suspend + '}';
    }
}
