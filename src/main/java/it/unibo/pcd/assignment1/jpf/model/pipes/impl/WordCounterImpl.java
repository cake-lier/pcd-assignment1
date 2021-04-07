package it.unibo.pcd.assignment1.jpf.model.pipes.impl;

import it.unibo.pcd.assignment1.jpf.model.entities.Update;
import it.unibo.pcd.assignment1.jpf.model.entities.impl.UpdateImpl;
import it.unibo.pcd.assignment1.jpf.model.pipes.WordCounter;

import java.util.HashMap;
import java.util.Map;

public class WordCounterImpl extends BoundedPipe<Update> implements WordCounter {
    private final Map<String, Long> frequencies;
    private int processedWords;

    public WordCounterImpl(final int maxNumberOfElements) {
        super(maxNumberOfElements);
        this.frequencies = new HashMap<>();
    }

    @Override
    protected void doEnqueue(final Update element) {
        element.getFrequencies().forEach((k, v) -> this.frequencies.merge(k, v, (o, n) -> o + n));
        this.processedWords += element.getProcessedWords();
        super.doEnqueue(new UpdateImpl(this.frequencies, this.processedWords));
    }

}
